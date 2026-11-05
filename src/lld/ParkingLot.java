package lld;

/*
    ref : https://chatgpt.com/share/6aebe8c4-eb10-83ee-9136-9ae904349e6d
    VehicleType
        BIKE, CAR, TRUCK

    Vehicle
        - number
        - type
        + getNumber()
        + getType()

ParkingLot
- Map<floorId, ParkingFloor>
- activeTickets

+ park()
+ unpark()


--------x----x--------------
ParkingFloor
- floorId
- Map<slotId, ParkingSlot>

+ getFreeSlot()

--------x----x--------------
ParkingSlot
- slotId
- vehicleType
- isFree
- parkedVehicle

+ park()
+ unpark()

--------x----x--------------
ParkingTicket
- ticketId
- floorId
- slotId
- vehicleNumber


--------x----x--------------
IParkingSlotStrategy
+ getFreeSlot()

--------x----x--------------
FirstAvailableSlotStrategy implements IParkingSlotStrategy
+ getFreeSlot()

--------x----x--------------
NearestSlotStrategy implements IParkingSlotStrategy
+ getFreeSlot()




*/
public class ParkingLot {
}
