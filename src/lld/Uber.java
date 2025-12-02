package lld;

/*
    1. Strategy Pattern
        - Used for: Pricing (Standard, Surge, Pool)
        - RideMatchingService -> findDriverStrategy

    2. Observer Pattern
        - Notifications (ride created, driver assigned, ride completed)

    3. Factory Pattern
        - Used for: Creating different ride types (UberGo, UberX, UberMoto)

    4. Singleton Pattern
        - Used for:
        - Matching service
        - Driver location service
        - Payment gateway client
        - Notification service

    5. Decorator Pattern
        - Often used when adding optional features like:
        - Toll charges
        - Promo codes
        - Taxes
        - Subscription perks (Uber One)

    ==Thread-safety==
    - Threadsafe singletons
    - ConcurrentHashMap<String, Driver> drivers

*/
public class Uber {

}
