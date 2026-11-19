
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// ==================== USER ====================

class User {
    int id;
    String name;
    String email;
    Calendar calendar;

    User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.calendar = new Calendar(this);
    }
}

// ==================== TIME SLOT ====================

class TimeSlot {
    LocalDateTime startTime;
    LocalDateTime endTime;

    TimeSlot(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null || endTime == null
                || !startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Invalid time slot");
        }

        this.startTime = startTime;
        this.endTime = endTime;
    }

    boolean overlaps(TimeSlot other) {
        return startTime.isBefore(other.endTime)
            && other.startTime.isBefore(endTime);
    }

    long getDurationMinutes() {
        return Duration.between(startTime, endTime).toMinutes();
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime;
    }
}

// ==================== MEETING ====================

enum MeetingStatus {
    SCHEDULED, CANCELLED, COMPLETED
}

enum InvitationStatus {
    PENDING, ACCEPTED, DECLINED
}

class Meeting {
    int id;
    String title;
    String description;
    User organizer;
    List<User> participants;
    TimeSlot timeSlot;
    MeetingStatus status;

    Map<Integer, InvitationStatus> invitations = new HashMap<>();

    Meeting(
        int id,
        String title,
        String description,
        User organizer,
        List<User> participants,
        TimeSlot timeSlot
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.organizer = organizer;
        this.participants = new ArrayList<>(participants);
        this.timeSlot = timeSlot;
        this.status = MeetingStatus.SCHEDULED;

        for (User participant : participants) {
            invitations.put(participant.id, InvitationStatus.PENDING);
        }
    }

    @Override
    public String toString() {
        return "Meeting{id=" + id +
            ", title='" + title + '\'' +
            ", timeSlot=" + timeSlot +
            ", status=" + status + '}';
    }
}

// ==================== CALENDAR ====================

class Calendar {
    User owner;
    List<Meeting> meetings = new ArrayList<>();

    Calendar(User owner) {
        this.owner = owner;
    }

    synchronized boolean isAvailable(TimeSlot requestedSlot) {
        for (Meeting meeting : meetings) {
            if (meeting.status == MeetingStatus.CANCELLED) {
                continue;
            }

            if (meeting.timeSlot.overlaps(requestedSlot)) {
                return false;
            }
        }

        return true;
    }

    synchronized void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    synchronized void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    synchronized List<Meeting> getMeetings() {
        return new ArrayList<>(meetings);
    }

    synchronized List<TimeSlot> getAvailableSlots(
        LocalDateTime start,
        LocalDateTime end,
        long durationMinutes
    ) {
        if (durationMinutes <= 0 || !start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid search range");
        }

        List<Meeting> sorted = new ArrayList<>();

        for (Meeting meeting : meetings) {
            if (meeting.status != MeetingStatus.CANCELLED
                    && meeting.timeSlot.endTime.isAfter(start)
                    && meeting.timeSlot.startTime.isBefore(end)) {
                sorted.add(meeting);
            }
        }

        sorted.sort(
            Comparator.comparing(m -> m.timeSlot.startTime)
        );

        List<TimeSlot> availableSlots = new ArrayList<>();

        LocalDateTime current = start;

        for (Meeting meeting : sorted) {
            LocalDateTime meetingStart = meeting.timeSlot.startTime;
            LocalDateTime meetingEnd = meeting.timeSlot.endTime;

            if (current.isBefore(meetingStart)) {
                LocalDateTime gapEnd =
                    meetingStart.isBefore(end) ? meetingStart : end;

                if (Duration.between(current, gapEnd).toMinutes()
                        >= durationMinutes) {
                    availableSlots.add(
                        new TimeSlot(
                            current,
                            current.plusMinutes(durationMinutes)
                        )
                    );
                }
            }

            if (meetingEnd.isAfter(current)) {
                current = meetingEnd;
            }

            if (!current.isBefore(end)) {
                break;
            }
        }

        if (current.isBefore(end)
                && Duration.between(current, end).toMinutes()
                    >= durationMinutes) {
            availableSlots.add(
                new TimeSlot(
                    current,
                    current.plusMinutes(durationMinutes)
                )
            );
        }

        return availableSlots;
    }
}

// ==================== MEETING FACTORY ====================

class MeetingFactory {

    static Meeting createMeeting(
        int id,
        String title,
        String description,
        User organizer,
        List<User> participants,
        TimeSlot timeSlot
    ) {
        return new Meeting(
            id, title, description, organizer, participants, timeSlot
        );
    }
}

// ==================== SCHEDULING STRATEGY ====================

interface SchedulingStrategy {
    boolean schedule(Meeting meeting);
}

class AvailabilityBasedScheduling implements SchedulingStrategy {

    @Override
    public boolean schedule(Meeting meeting) {
        Set<Integer> checkedUsers = new HashSet<>();

        checkedUsers.add(meeting.organizer.id);

        if (!meeting.organizer.calendar.isAvailable(meeting.timeSlot)) {
            return false;
        }

        for (User participant : meeting.participants) {
            if (!checkedUsers.add(participant.id)) {
                return false;
            }

            if (!participant.calendar.isAvailable(meeting.timeSlot)) {
                return false;
            }
        }

        meeting.organizer.calendar.addMeeting(meeting);

        for (User participant : meeting.participants) {
            participant.calendar.addMeeting(meeting);
        }

        return true;
    }
}

// ==================== MEETING SCHEDULER SINGLETON ====================

class MeetingScheduler {

    private static volatile MeetingScheduler instance;

    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final Map<Integer, Meeting> meetings = new ConcurrentHashMap<>();

    private int meetingId = 1;

    private MeetingScheduler() {}

    public static MeetingScheduler getInstance() {
        if (instance == null) {
            synchronized (MeetingScheduler.class) {
                if (instance == null) {
                    instance = new MeetingScheduler();
                }
            }
        }

        return instance;
    }

    // Register user
    void addUser(User user) {
        users.put(user.id, user);
    }

    // Create and schedule meeting
    synchronized Meeting createMeeting(
        String title,
        String description,
        int organizerId,
        List<Integer> participantIds,
        TimeSlot timeSlot
    ) {
        User organizer = users.get(organizerId);

        if (organizer == null || participantIds == null) {
            throw new IllegalArgumentException("Invalid organizer or participants");
        }

        List<User> participants = new ArrayList<>();
        Set<Integer> uniqueIds = new HashSet<>();

        uniqueIds.add(organizerId);

        for (Integer participantId : participantIds) {
            User participant = users.get(participantId);

            if (participant == null || !uniqueIds.add(participantId)) {
                throw new IllegalArgumentException("Invalid or duplicate participant");
            }

            participants.add(participant);
        }

        Meeting meeting = MeetingFactory.createMeeting(
            meetingId++,
            title,
            description,
            organizer,
            participants,
            timeSlot
        );

        SchedulingStrategy strategy = new AvailabilityBasedScheduling();

        if (!strategy.schedule(meeting)) {
            throw new RuntimeException("One or more participants are unavailable");
        }

        meetings.put(meeting.id, meeting);

        return meeting;
    }

    // Accept or decline invitation
    boolean respondToInvitation(
        int meetingId,
        int userId,
        InvitationStatus response
    ) {
        Meeting meeting = meetings.get(meetingId);

        if (meeting == null || response == InvitationStatus.PENDING) {
            return false;
        }

        synchronized (meeting) {
            if (meeting.status != MeetingStatus.SCHEDULED
                    || !meeting.invitations.containsKey(userId)
                    || meeting.invitations.get(userId) != InvitationStatus.PENDING) {
                return false;
            }

            meeting.invitations.put(userId, response);
            return true;
        }
    }

    // Reschedule meeting
    synchronized boolean rescheduleMeeting(
        int meetingId,
        int organizerId,
        TimeSlot newTimeSlot
    ) {
        Meeting meeting = meetings.get(meetingId);

        if (meeting == null || meeting.organizer.id != organizerId) {
            return false;
        }

        synchronized (meeting) {
            if (meeting.status != MeetingStatus.SCHEDULED) {
                return false;
            }

            // Temporarily remove old meeting from calendars.
            meeting.organizer.calendar.removeMeeting(meeting);

            for (User participant : meeting.participants) {
                participant.calendar.removeMeeting(meeting);
            }

            TimeSlot oldTimeSlot = meeting.timeSlot;
            meeting.timeSlot = newTimeSlot;

            SchedulingStrategy strategy = new AvailabilityBasedScheduling();

            if (strategy.schedule(meeting)) {
                for (User participant : meeting.participants) {
                    meeting.invitations.put(
                        participant.id, InvitationStatus.PENDING
                    );
                }

                return true;
            }

            // Roll back to old time slot.
            meeting.timeSlot = oldTimeSlot;

            meeting.organizer.calendar.addMeeting(meeting);

            for (User participant : meeting.participants) {
                participant.calendar.addMeeting(meeting);
            }

            return false;
        }
    }

    // Cancel meeting
    boolean cancelMeeting(int meetingId, int organizerId) {
        Meeting meeting = meetings.get(meetingId);

        if (meeting == null || meeting.organizer.id != organizerId) {
            return false;
        }

        synchronized (meeting) {
            if (meeting.status != MeetingStatus.SCHEDULED) {
                return false;
            }

            meeting.status = MeetingStatus.CANCELLED;

            meeting.organizer.calendar.removeMeeting(meeting);

            for (User participant : meeting.participants) {
                participant.calendar.removeMeeting(meeting);
            }

            return true;
        }
    }

    // Get user calendar
    List<Meeting> getCalendar(int userId) {
        User user = users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user.calendar.getMeetings();
    }

    // Find available slots for a user
    List<TimeSlot> getAvailableSlots(
        int userId,
        LocalDateTime start,
        LocalDateTime end,
        long durationMinutes
    ) {
        User user = users.get(userId);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return user.calendar.getAvailableSlots(
            start, end, durationMinutes
        );
    }

    // Find common available slots for all participants.
    List<TimeSlot> findCommonSlots(
        List<Integer> userIds,
        LocalDateTime start,
        LocalDateTime end,
        long durationMinutes
    ) {
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("No participants provided");
        }

        List<TimeSlot> commonSlots = new ArrayList<>();

        LocalDateTime candidate = start;

        while (candidate.plusMinutes(durationMinutes).isBefore(end)
                || candidate.plusMinutes(durationMinutes).equals(end)) {

            TimeSlot requested = new TimeSlot(
                candidate,
                candidate.plusMinutes(durationMinutes)
            );

            boolean available = true;

            for (Integer userId : userIds) {
                User user = users.get(userId);

                if (user == null || !user.calendar.isAvailable(requested)) {
                    available = false;
                    break;
                }
            }

            if (available) {
                commonSlots.add(requested);
            }

            candidate = candidate.plusMinutes(30);
        }

        return commonSlots;
    }

    Meeting getMeeting(int meetingId) {
        return meetings.get(meetingId);
    }
}

// ==================== MAIN ====================

public class MeetingSchedulerApp {

    public static void main(String[] args) {

        MeetingScheduler scheduler = MeetingScheduler.getInstance();

        // Create users
        User chenna = new User(1, "Chenna", "chenna@example.com");
        User rahul = new User(2, "Rahul", "rahul@example.com");
        User amit = new User(3, "Amit", "amit@example.com");

        scheduler.addUser(chenna);
        scheduler.addUser(rahul);
        scheduler.addUser(amit);

        // Meeting time
        TimeSlot slot = new TimeSlot(
            LocalDateTime.of(2026, 11, 20, 10, 0),
            LocalDateTime.of(2026, 11, 20, 11, 0)
        );

        // Create meeting
        Meeting meeting = scheduler.createMeeting(
            "System Design Discussion",
            "Discuss architecture and scalability",
            1,
            Arrays.asList(2, 3),
            slot
        );

        System.out.println("Meeting Created:");
        System.out.println(meeting);

        // Accept invitation
        scheduler.respondToInvitation(
            meeting.id, 2, InvitationStatus.ACCEPTED
        );

        // Decline invitation
        scheduler.respondToInvitation(
            meeting.id, 3, InvitationStatus.DECLINED
        );

        System.out.println("\nInvitations:");
        System.out.println(meeting.invitations);

        // Try creating overlapping meeting
        try {
            TimeSlot conflictingSlot = new TimeSlot(
                LocalDateTime.of(2026, 11, 20, 10, 30),
                LocalDateTime.of(2026, 11, 20, 11, 30)
            );

            scheduler.createMeeting(
                "Conflicting Meeting",
                "This should fail",
                1,
                Arrays.asList(2),
                conflictingSlot
            );

        } catch (Exception e) {
            System.out.println("\nConflict: " + e.getMessage());
        }

        // Find common availability
        List<TimeSlot> commonSlots = scheduler.findCommonSlots(
            Arrays.asList(1, 2, 3),
            LocalDateTime.of(2026, 11, 20, 9, 0),
            LocalDateTime.of(2026, 11, 20, 18, 0),
            60
        );

        System.out.println("\nCommon Available Slots:");

        for (TimeSlot available : commonSlots) {
            System.out.println(available);
        }

        // Reschedule meeting
        TimeSlot newSlot = new TimeSlot(
            LocalDateTime.of(2026, 11, 20, 12, 0),
            LocalDateTime.of(2026, 11, 20, 13, 0)
        );

        boolean rescheduled = scheduler.rescheduleMeeting(
            meeting.id, 1, newSlot
        );

        System.out.println("\nRescheduled: " + rescheduled);
        System.out.println(scheduler.getMeeting(meeting.id));

        // Cancel meeting
        boolean cancelled = scheduler.cancelMeeting(meeting.id, 1);

        System.out.println("\nCancelled: " + cancelled);
    }
}
