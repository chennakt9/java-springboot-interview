package lld;

/*

table Users
    - user_id : VARCHAR (PK)
    - name : VARCHAR
    - email : VARCHAR (unique)
    - wallet_balance : DECIMAL(10,2)

table Passengers
    - passenger_id : VARCHAR (PK)
    - name : VARCHAR
    - age : INT
    - gender : VARCHAR
    - booking_id : VARCHAR (FK -> Bookings.booking_id)

table Trains
    - train_id : VARCHAR (PK)
    - train_name : VARCHAR
    - source : VARCHAR
    - destination : VARCHAR

table TrainScheduleTemplate
    - schedule_id : VARCHAR (PK)
    - train_id : VARCHAR (FK -> Trains.train_id)
    - day_of_week : INT          // 1=Mon ... 7=Sun
    - departure_time : TIME
    - arrival_time : TIME

table TrainRuns
    - run_id : VARCHAR (PK)
    - train_id : VARCHAR (FK -> Trains.train_id)
    - run_date : DATE
    - departure_time : TIME
    - arrival_time : TIME
    - status : VARCHAR            // SCHEDULED / CANCELLED / DELAYED

table Coaches
    - coach_id : VARCHAR (PK)
    - train_id : VARCHAR (FK -> Trains.train_id)
    - coach_type : VARCHAR

table Seats
    - seat_id : VARCHAR (PK)
    - coach_id : VARCHAR (FK -> Coaches.coach_id)
    - seat_number : VARCHAR

table SeatInventory
    - id : VARCHAR (PK)
    - train_id : VARCHAR (FK -> Trains.train_id)
    - run_id : VARCHAR (FK -> TrainRuns.run_id)
    - seat_id : VARCHAR (FK -> Seats.seat_id)
    - booking_id : VARCHAR (nullable, FK -> Bookings.booking_id)
    - is_booked : BOOLEAN

table Bookings
    - booking_id : VARCHAR (PK)
    - user_id : VARCHAR (FK -> Users.user_id)
    - train_id : VARCHAR (FK -> Trains.train_id)
    - run_id : VARCHAR (FK -> TrainRuns.run_id)
    - status : VARCHAR
    - total_fare : DECIMAL(10,2)

table BookingSeats
    - id : VARCHAR (PK)
    - booking_id : VARCHAR (FK -> Bookings.booking_id)
    - seat_id : VARCHAR (FK -> Seats.seat_id)

table WalletTransactions
    - transaction_id : VARCHAR (PK)
    - user_id : VARCHAR (FK -> Users.user_id)
    - amount : DECIMAL(10,2)
    - type : VARCHAR
    - timestamp : TIMESTAMP

table Notifications
    - notification_id : VARCHAR (PK)
    - user_id : VARCHAR (FK -> Users.user_id)
    - message : TEXT
    - created_at : TIMESTAMP

Only SeatInventory changes per date.

For each train, we generate seat inventory for a run:

Seat 1 → booked?

Seat 2 → free?

Seat 3 → RAC?

This is the per-date table.

==Visual Representation==
Train (static)
   |
   +-- Coaches (static)
           |
           +-- Seats (static)
                   |
                   +-- SeatInventory (dynamic per DATE or per RUN)

*/
public class Train {
}
