
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// ==================== MOVIE ====================

class Movie {
    int id;
    String name;
    String language;
    int duration;

    Movie(int id, String name, String language, int duration) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.duration = duration;
    }
}

// ==================== THEATRE ====================

class Theatre {
    int id;
    String name;
    String city;
    List<Screen> screens = new ArrayList<>();

    Theatre(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    void addScreen(Screen screen) {
        screens.add(screen);
    }
}

// ==================== SCREEN ====================

class Screen {
    int id;
    String name;
    List<Seat> seats = new ArrayList<>();

    Screen(int id, String name) {
        this.id = id;
        this.name = name;
    }

    void addSeat(Seat seat) {
        seats.add(seat);
    }
}

// ==================== SEAT ====================

enum SeatType {
    REGULAR, PREMIUM, RECLINER
}

class Seat {
    int id;
    String row;
    int number;
    SeatType type;
    double price;

    Seat(int id, String row, int number, SeatType type, double price) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.type = type;
        this.price = price;
    }

    public String toString() {
        return row + number;
    }
}

// ==================== SHOW ====================

class Show {
    int id;
    Movie movie;
    Screen screen;
    Theatre theatre;
    long startTime;

    // Seat ID -> Booking ID
    Map<Integer, Integer> bookedSeats = new HashMap<>();

    Show(int id, Movie movie, Screen screen, Theatre theatre, long startTime) {
        this.id = id;
        this.movie = movie;
        this.screen = screen;
        this.theatre = theatre;
        this.startTime = startTime;
    }

    synchronized boolean reserveSeats(List<Seat> seats, int bookingId) {
        for (Seat seat : seats) {
            if (bookedSeats.containsKey(seat.id)) {
                return false;
            }
        }

        for (Seat seat : seats) {
            bookedSeats.put(seat.id, bookingId);
        }

        return true;
    }

    synchronized void releaseSeats(List<Seat> seats, int bookingId) {
        for (Seat seat : seats) {
            if (Objects.equals(bookedSeats.get(seat.id), bookingId)) {
                bookedSeats.remove(seat.id);
            }
        }
    }

    synchronized boolean areSeatsAvailable(List<Seat> seats) {
        for (Seat seat : seats) {
            if (bookedSeats.containsKey(seat.id)) {
                return false;
            }
        }
        return true;
    }
}

// ==================== STRATEGY: SEAT SELECTION ====================

interface SeatSelectionStrategy {
    List<Seat> selectSeats(Show show, int count);
}

class CheapestSeatSelection implements SeatSelectionStrategy {

    public List<Seat> selectSeats(Show show, int count) {
        List<Seat> available = new ArrayList<>();

        for (Seat seat : show.screen.seats) {
            if (!show.bookedSeats.containsKey(seat.id)) {
                available.add(seat);
            }
        }

        available.sort(Comparator.comparingDouble(s -> s.price));

        if (available.size() < count) {
            return Collections.emptyList();
        }

        return new ArrayList<>(available.subList(0, count));
    }
}

class PremiumSeatSelection implements SeatSelectionStrategy {

    public List<Seat> selectSeats(Show show, int count) {
        List<Seat> available = new ArrayList<>();

        for (Seat seat : show.screen.seats) {
            if (!show.bookedSeats.containsKey(seat.id)
                    && seat.type == SeatType.PREMIUM) {
                available.add(seat);
            }
        }

        if (available.size() < count) {
            return Collections.emptyList();
        }

        return new ArrayList<>(available.subList(0, count));
    }
}

// ==================== PAYMENT ====================

enum PaymentStatus {
    SUCCESS, FAILED
}

interface PaymentStrategy {
    PaymentStatus pay(double amount);
}

class UpiPayment implements PaymentStrategy {

    public PaymentStatus pay(double amount) {
        System.out.println("Paid Rs." + amount + " using UPI");
        return PaymentStatus.SUCCESS;
    }
}

class CardPayment implements PaymentStrategy {

    public PaymentStatus pay(double amount) {
        System.out.println("Paid Rs." + amount + " using Card");
        return PaymentStatus.SUCCESS;
    }
}

class NetBankingPayment implements PaymentStrategy {

    public PaymentStatus pay(double amount) {
        System.out.println("Paid Rs." + amount + " using Net Banking");
        return PaymentStatus.SUCCESS;
    }
}

// ==================== FACTORY ====================

class PaymentFactory {

    static PaymentStrategy getPaymentStrategy(String type) {
        switch (type.toUpperCase()) {
            case "UPI":
                return new UpiPayment();

            case "CARD":
                return new CardPayment();

            case "NET_BANKING":
                return new NetBankingPayment();

            default:
                throw new IllegalArgumentException("Invalid payment type");
        }
    }
}

// ==================== BOOKING ====================

enum BookingStatus {
    PENDING, CONFIRMED, FAILED, CANCELLED
}

class Booking {
    int id;
    Show show;
    List<Seat> seats;
    double amount;
    BookingStatus status;

    Booking(int id, Show show, List<Seat> seats) {
        this.id = id;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.amount = seats.stream().mapToDouble(s -> s.price).sum();
        this.status = BookingStatus.PENDING;
    }
}

// ==================== BOOKING MANAGER (SINGLETON) ====================

class BookingManager {

    private static volatile BookingManager instance;

    private final Map<Integer, Movie> movies = new ConcurrentHashMap<>();
    private final Map<Integer, Theatre> theatres = new ConcurrentHashMap<>();
    private final Map<Integer, Show> shows = new ConcurrentHashMap<>();
    private final Map<Integer, Booking> bookings = new ConcurrentHashMap<>();

    private int bookingId = 1;

    private BookingManager() {}

    public static BookingManager getInstance() {
        if (instance == null) {
            synchronized (BookingManager.class) {
                if (instance == null) {
                    instance = new BookingManager();
                }
            }
        }
        return instance;
    }

    void addMovie(Movie movie) {
        movies.put(movie.id, movie);
    }

    void addTheatre(Theatre theatre) {
        theatres.put(theatre.id, theatre);
    }

    void addShow(Show show) {
        shows.put(show.id, show);
    }

    List<Show> getShowsByMovie(int movieId) {
        List<Show> result = new ArrayList<>();

        for (Show show : shows.values()) {
            if (show.movie.id == movieId) {
                result.add(show);
            }
        }

        return result;
    }

    List<Seat> getAvailableSeats(int showId) {
        Show show = shows.get(showId);
        List<Seat> result = new ArrayList<>();

        synchronized (show) {
            for (Seat seat : show.screen.seats) {
                if (!show.bookedSeats.containsKey(seat.id)) {
                    result.add(seat);
                }
            }
        }

        return result;
    }

    Booking createBooking(int showId, List<Seat> seats) {
        Show show = shows.get(showId);

        if (show == null || seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("Invalid show or seats");
        }

        int id;
        synchronized (this) {
            id = bookingId++;
        }

        // Atomically reserve all requested seats.
        if (!show.reserveSeats(seats, id)) {
            throw new RuntimeException("One or more seats are unavailable");
        }

        Booking booking = new Booking(id, show, seats);
        bookings.put(id, booking);

        return booking;
    }

    boolean makePayment(int bookingId, String paymentType) {
        Booking booking = bookings.get(bookingId);

        if (booking == null || booking.status != BookingStatus.PENDING) {
            return false;
        }

        PaymentStrategy payment = PaymentFactory.getPaymentStrategy(paymentType);

        PaymentStatus result;

        try {
            result = payment.pay(booking.amount);
        } catch (Exception e) {
            result = PaymentStatus.FAILED;
        }

        synchronized (booking) {
            if (result == PaymentStatus.SUCCESS) {
                booking.status = BookingStatus.CONFIRMED;
                return true;
            }

            booking.show.releaseSeats(booking.seats, booking.id);
            booking.status = BookingStatus.FAILED;
            return false;
        }
    }

    boolean cancelBooking(int bookingId) {
        Booking booking = bookings.get(bookingId);

        if (booking == null) {
            return false;
        }

        synchronized (booking) {
            if (booking.status != BookingStatus.CONFIRMED) {
                return false;
            }

            booking.show.releaseSeats(booking.seats, booking.id);
            booking.status = BookingStatus.CANCELLED;
            return true;
        }
    }

    Booking getBooking(int bookingId) {
        return bookings.get(bookingId);
    }
}

// ==================== MAIN ====================

public class BookMyShow {

    public static void main(String[] args) {

        BookingManager manager = BookingManager.getInstance();

        // Create movie
        Movie movie = new Movie(1, "Avengers", "English", 180);
        manager.addMovie(movie);

        // Create theatre
        Theatre theatre = new Theatre(1, "PVR", "Bangalore");

        // Create screen
        Screen screen = new Screen(1, "Screen 1");

        // Create seats
        screen.addSeat(new Seat(1, "A", 1, SeatType.REGULAR, 150));
        screen.addSeat(new Seat(2, "A", 2, SeatType.REGULAR, 150));
        screen.addSeat(new Seat(3, "B", 1, SeatType.PREMIUM, 250));
        screen.addSeat(new Seat(4, "B", 2, SeatType.PREMIUM, 250));

        theatre.addScreen(screen);
        manager.addTheatre(theatre);

        // Create show
        Show show = new Show(
            1, movie, screen, theatre, System.currentTimeMillis()
        );

        manager.addShow(show);

        // Display available seats
        System.out.println("Available Seats:");
        System.out.println(manager.getAvailableSeats(1));

        // Select seats using Strategy
        SeatSelectionStrategy selectionStrategy = new PremiumSeatSelection();

        List<Seat> selectedSeats = selectionStrategy.selectSeats(show, 2);

        System.out.println("Selected Seats: " + selectedSeats);

        // Create booking
        Booking booking = manager.createBooking(1, selectedSeats);

        System.out.println("Booking ID: " + booking.id);
        System.out.println("Amount: Rs." + booking.amount);

        // Make payment
        boolean success = manager.makePayment(booking.id, "UPI");

        if (success) {
            System.out.println("Booking Confirmed!");
        } else {
            System.out.println("Booking Failed!");
        }

        System.out.println("Booking Status: " + booking.status);

        // Cancel booking
        // manager.cancelBooking(booking.id);
    }
}
