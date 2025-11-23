package lld;

/*
    ref  : https://chatgpt.com/share/6922a6b6-e414-800e-9010-ebdc734fd758

    requirements:
    1. Create a locker reservation when courier arrives.
    2. Deposit parcel into a locker (mark locker occupied).
    3. Customer pick-up using OTP/QR; record pickup.
    4. Locker expiry (auto-unlock or return-to-sender after TTL).
    5. Notifications (SMS/email/push) on deposit, expiration, pickup.
    6. Query locker status & history.

    Flow
    1. DB schema
    2. API design
    3. Classes / Design patterns

    API Design
    - POST /api/v1/reservations - courier requests a reservation
        - body: { trackingId, size, stationId, courierId, recipientPhone }
        - response : locker_unit_id
    - POST /api/v1/deposit - courier confirms deposit (or locker controller calls)
        - body: { reservationId, lockerUnitId, authTokenFromEdge }
    - POST /api/v1/pickup — user picks up using OTP/QR
        - body: { reservationId, otp }
    - GET /api/v1/stations/{id}/status — get lockers status

    DB schema
    1. LockerStation
        - id
        - location
        ...
    2. LockerUnit
        - id
        - locker_station_id
        - size (S/M/L)
        - status (AVL / BOOKED)
    3. Parcel
        - id
        - sender_info
        - receiver_info
    4. Reservation
        - parcel_id
        - locker_unit_id
        - otp
        - otp_expiry
        - reserved_at
        - deposited_at
        - picked_up_at
        - expires_at
        - status
        - assigned_by
    4. User
    5. AuditLog

    Classes / Services
    - LockerStrategy
        - getAvailableLocker
    - LockerService
        - createReservation()
        - deposit()
        - pickup()
    - NotificationService (Observer pattern / Pub sub)
        - notifyRecipient

    Followups
    Q: How to scale when many lockers & stations?
    A: Partition by station — each station handled by a small set of service instances; sharding DB by station region; use local edge controller for low-latency actuation; use async event pipelines for analytics.

    Q: How do you ensure no double-assignment?
    A: Use DB transaction with SELECT ... FOR UPDATE on locker row when picking; or use Redis-based distributed lock (RedLock) to ensure single-writer for station.

    Q: What if OTP is brute-forced?
    A: Rate-limit attempts per reservation, increase OTP complexity or use QR tied to device ID, and short TTLs. Log suspicious tries and alert.

    Q: How to perform reconciliation if edge and server diverge?
    A: Have periodic reconciliation job: edge reports actual locker states; server compares to DB and corrects statuses or raise admin alerts.

    Q: How to handle offline deposits by courier?
    A: Edge controller can accept deposit locally and persist a signed receipt. Once network returns, reconcile with server; server verifies signature and updates.

    Q: Database schema choice?
    A: Use relational DB (Postgres) for transactions; add Redis for caching and locks. Event-sourcing can be used to audit all state transitions.
*/
public class AmazonLocker {
}
