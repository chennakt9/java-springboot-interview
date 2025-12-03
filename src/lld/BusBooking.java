package lld;

/*
    Bus
        - id : String
        - operatorName : String
        - seatLayout : Map<Seat, SeatInfo>

    Trip
        - id : String
        - bus : Bus
        - source: String
        - destination: String
        - seats : Map<Seat, Booking>

    Seat
        - id
        - type
        - isAvailable

    SeatLockManager
        - lockedSeats : Map<String, Long> // tripId#seatId
        + lockSeat(trip, seat, userId)
        + unlockSeat(trip, seat)
        + isLocked(trip, seat)
        + autoReleaseExpiredLocks()

    FareStrategy
        - FlatFareStrategy
        - SurgeFareStrategy

    BookingService
        - initiateBooking (userId, trip, seats) : Booking
        - confirmBooking (bookingId, paymentDetails) : Ticket

    GET /search?source={}&destination={}&journeyDate={}
    -> another helper table->
    trip_inventory
        trip_id (PK)
        total_seats
        available_seats
        booked_seats
        updated_at


    SELECT
        t.id AS trip_id,
        b.operator_name,
        t.departure_time,
        t.arrival_time,
        ti.available_seats
    FROM trip t
    JOIN bus b ON b.id = t.bus_id
    JOIN trip_inventory ti ON ti.trip_id = t.id
    WHERE
        t.source = $1
        AND t.destination = $2
        AND t.journey_date = $3
    ORDER BY t.departure_time;

    GET /trips/{trip_id}/seats
    SELECT
        s.seat_number
    FROM seat s
    WHERE s.trip_id = $1
      AND s.status = 'AVAILABLE'
    ORDER BY s.seat_number;

*/
public class BusBooking {
}
