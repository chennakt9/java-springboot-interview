package lld;

/*

1. FACTORY PATTERN
-------------------
Use Case:
- Creating different payment method handlers.
  (UPI, CARD, WALLET, COD)

Reason:
- Each payment method has different validation and processing logic.
- Factory cleanly creates the right object.


2. STRATEGY PATTERN
---------------------
Use Cases:
- Delivery fee calculation strategies:
    -> NormalChargeStrategy
    -> SurgeChargeStrategy
    -> RainChargeStrategy
    -> FestivalChargeStrategy

- Search / Sorting strategies:
    -> SortByRating
    -> SortByDistance
    -> SortByDeliveryTime

- Recommendation strategies:
    -> PopularItemsStrategy
    -> UserPreferenceStrategy

Reason:
- Avoids repeated if-else logic.
- Makes logic plug-and-play.


3. OBSERVER PATTERN
---------------------
Use Cases:
- Order status updates:
    -> Notify customer
    -> Notify restaurant
    -> Notify delivery partner

- Notification system:
    -> SMS
    -> Email
    -> Push notifications

Reason:
- Decouples the order service from notification services.
- Supports real-time updates.


4. SINGLETON PATTERN
----------------------
Use Cases:
- Database connection pool
- Redis cache manager
- Payment gateway client (Razorpay, Stripe)
- Config manager

Reason:
- Only one instance needed for the whole application.
- Saves memory and avoids resource locks.


5. BUILDER PATTERN
---------------------
Use Cases:
- Constructing order objects:
    -> Items
    -> Add-ons
    -> Quantity
    -> Taxes
    -> Delivery charges

Reason:
- Easy to build complex objects with optional parts.

6. REPOSITORY PATTERN
------------------------
Use Cases:
- Data access layer:
    -> UserRepository
    -> RestaurantRepository
    -> MenuRepository
    -> OrderRepository

Reason:
- Separates database logic from business logic.
- Makes testing & swapping DB easier.


7. STATE PATTERN
-------------------
Use Case:
Order lifecycle:
PLACED → CONFIRMED → PREPARING → PICKED_UP → DELIVERED → COMPLETED

Reason:
- Each state has unique rules.
- Prevents illegal transitions.
- Avoids switch-case monster code.


8. ADAPTER PATTERN
---------------------
Use Cases:
- Integrating external services:
    -> Google Maps API
    -> SMS/Email providers
    -> Payment gateways
    -> Notification push provider

Reason:
- Wraps third-party APIs behind a stable internal interface.


9. COMMAND PATTERN (Optional)
-------------------------------
Use Cases:
- Logging
- Payment retry queue
- Background tasks:
    -> Assign delivery partner
    -> Compute ETA
    -> Send invoice

Reason:
- Useful for event queues and delayed execution.


===========================================
END OF FILE
===========================================

===========================================================
FOOD DELIVERY SYSTEM – CLASS DIAGRAM WITH DESIGN PATTERNS
===========================================================


-------------------------
USER MODULE
-------------------------

Class: User
- userId
- name
- phone
- email
- role
- addresses : List<Address>
+ addAddress()
+ getAddresses()

Class: Address
- addressId
- line1
- line2
- city
- state
- pincode
- lat
- lng


-------------------------
RESTAURANT MODULE
-------------------------

Class: Restaurant
- restaurantId
- owner : User
- name
- description
- cuisines : List<String>
- menuItems : List<MenuItem>
- avgRating
- isOpen
- openingTime
- closingTime
+ addMenuItem()
+ updateAvailability()

Class: MenuItem
- itemId
- name
- description
- price
- isAvailable
- type
- options : List<ItemOption>
+ addOption()

Class: ItemOption
- optionId
- name
- price


-------------------------
CART MODULE
-------------------------

Class: Cart
- cartId
- user : User
- restaurant : Restaurant
- items : List<CartItem>
+ addItem()
+ removeItem()
+ getTotal()

Class: CartItem
- cartItemId
- menuItem : MenuItem
- quantity
- totalPrice


-------------------------
ORDER MODULE
-------------------------

Class: Order   (STATE PATTERN)
- orderId
- user : User
- restaurant : Restaurant
- deliveryPartner : DeliveryPartner
- items : List<OrderItem>
- status
- totalAmount
- deliveryFee
- deliveryAddress : Address
- payment : Payment
+ updateStatus()    <-- delegates to state object

Class: OrderState   (STATE PATTERN)
+ handle()

Class: PlacedState        implements OrderState
Class: ConfirmedState     implements OrderState
Class: PreparingState     implements OrderState
Class: PickedUpState      implements OrderState
Class: DeliveredState     implements OrderState
Class: CancelledState     implements OrderState

Class: OrderItem
- orderItemId
- itemName
- pricePerUnit
- quantity
- totalPrice


-------------------------
DELIVERY MODULE
-------------------------

Class: DeliveryPartner
- partnerId
- user : User
- currentLat
- currentLng
- isAvailable
+ updateLocation()

Class: DeliveryTracking
- trackingId
- order : Order
- deliveryPartner : DeliveryPartner
- currentLat
- currentLng
- status
+ updateStatus()
+ updateLocation()


-------------------------
PAYMENT MODULE
-------------------------

Class: Payment   (STRATEGY + FACTORY)
- paymentId
- orderId
- amount
- status
- method
+ process()

Class: PaymentStrategy     (STRATEGY PATTERN)
+ pay(amount)

Class: UPIPaymentStrategy       implements PaymentStrategy
Class: CardPaymentStrategy      implements PaymentStrategy
Class: WalletPaymentStrategy    implements PaymentStrategy
Class: CODPaymentStrategy       implements PaymentStrategy

Class: PaymentFactory       (FACTORY PATTERN)
+ createPayment(method)


-------------------------
DELIVERY FEE STRATEGIES
-------------------------

Class: DeliveryFeeStrategy        (STRATEGY PATTERN)
+ calculate(order)

Class: NormalFeeStrategy          implements DeliveryFeeStrategy
Class: SurgeFeeStrategy           implements DeliveryFeeStrategy
Class: RainFeeStrategy            implements DeliveryFeeStrategy
Class: FestivalFeeStrategy        implements DeliveryFeeStrategy


-------------------------
SEARCH & SORT STRATEGIES
-------------------------

Class: SearchStrategy         (STRATEGY PATTERN)
+ search(restaurants)

Class: SortByRatingStrategy           implements SearchStrategy
Class: SortByDistanceStrategy         implements SearchStrategy
Class: SortByDeliveryTimeStrategy     implements SearchStrategy


-------------------------
NOTIFICATION MODULE
-------------------------

Class: OrderEventSubject       (OBSERVER PATTERN)
- observers : List<Observer>
+ registerObserver()
+ removeObserver()
+ notifyAll()

Class: Observer               (OBSERVER PATTERN)
+ update(order)

Class: CustomerNotifier       implements Observer
Class: RestaurantNotifier     implements Observer
Class: DeliveryNotifier       implements Observer


-------------------------
INFRASTRUCTURE (SINGLETONS)
-------------------------

Class: DatabaseConnection    (SINGLETON PATTERN)
- instance
+ getInstance()

Class: RedisCacheManager     (SINGLETON PATTERN)
- instance
+ getInstance()

Class: PaymentGatewayClient  (SINGLETON PATTERN)
- instance
+ getInstance()


-------------------------
REPOSITORIES (REPOSITORY PATTERN)
-------------------------

Class: UserRepository
+ findById()
+ save()

Class: RestaurantRepository
+ findById()
+ search()
+ save()

Class: OrderRepository
+ findById()
+ save()
+ updateStatus()

Class: MenuRepository
+ findItemsByRestaurant()


===========================================================
END OF SINGLE FILE
===========================================================

*/
public class FoodDelivery {
}
