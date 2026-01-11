package lld;

/*
    ref : https://youtu.be/5F-3BzCuUS0?si=9tTDLPlUmTzR0WS-
    ==Requirements==
    - Users can book any meeting room from (start time to end time) if the room is available.
    - If a room is not available, it should be shown at that moment only.
    - All the participants of a meeting should receive a notification related to the same.
    - Store the history of all the meetings scheduled.
    - Users can see all the available meeting rooms for a particular time slot ( can be neglected for implementation but is good to discuss)

    User
    - userId
    - name
    - email
    + getBookings()

    MeetingRoom
    - roomId
    - name
    - capacity
    + isAvailable(startTime, endTime)
    + book(booking)

    Booking
    - bookingId
    - startTime
    - endTime
    - participants : List<User>
    - meetingRoom : MeetingRoom
    - selectedFeatures : List<String>
    + notifyParticipants()

    BookingRequest
    - startTime
    - endTime
    - participants : List<User>
    - tvRequired : boolean
    - whiteboardRequired : boolean
    - acRequired : boolean

    BookingRequestBuilder
    - startTime
    - endTime
    - participants : List<User>
    - tvRequired
    - whiteboardRequired
    - acRequired
    + setStartTime(start)
    + setEndTime(end)
    + addParticipant(user)
    + requireTV()
    + requireWhiteboard()
    + requireAC()
    + build() : BookingRequest

    RoomDecorator <abstract implements Room>
        - wrappedRoom : Room
        + getDescription()
        + isAvailable(startTime, endTime)
        + book(booking)

    TVDecorator <extends RoomDecorator>
        + getDescription()

    WhiteboardDecorator <extends RoomDecorator>
        + getDescription()

    ACDecorator <extends RoomDecorator>
        + getDescription()

    NotificationService
    - strategies : Map<type, NotificationStrategy>
    + update(booking)

    note:
        1. If we have optional features, use builder pattern along with decorator pattern
        2. Use strategy pattern for notification service

 MeetingRoom mr = MeetingRoomBuilder(sdkj, sakj, aklsjf);

 BasicRoom
    - mr

(BookingRequestBuilder.isTVRequired) == true {
    new TVDecorator(BasicRoom)
}

*/

public class MeetingRoom {
}
