package lld;

/*
    ref : https://chatgpt.com/share/6919c9d6-fd6c-800e-b759-8290c4fde5f0
    VehicleType
        BIKE, CAR, TRUCK

    Vehicle
        - number
        - type
        + getNumber()
        + getType()

    ParkingSlot
        - slotId
        - vehicleType
        - free
        - parkedVehicle

         + isFree()
         + getType()
         + getSlotId()

         + park()
         + unpark()

    ParkingFloor
        - floorId
        - slots

        + getFreeSlot()
        + getFloorId()

    ParkingTicket
        - ticketId
        - floorId
        - slotId
        - vehicleNumber

        + getTicketId()
        + getFloorId()
        + getSlotId()
        + getVehicleNumber()

    ParkingLot
        - floors
        - activeTickets

        + park()
        + unpark()


*/
public class ParkingLot {
}
