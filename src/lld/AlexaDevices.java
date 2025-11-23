package lld;

/*
    ref : https://chatgpt.com/share/6922be97-19f4-800e-9c30-04f5c9d36128

    Requirements
    Consider there are different types of alexa devices available.
    One with audio, one with screen, one with audio and screen.
    These devices may have a battery or may not.
    Battery devices will have battery percentage.
    Both battery and non battery devices can be charged.
    The task is to show the battery percentage.
    Include a show method and that method should show the current battery percentage if it has a battery.
    If not just say, battery is not available.
    You should also say whether its currently charging or not.

    Device
     ├── BatteryBehavior (strategy)
     ├── ChargingBehavior (strategy)
     └── ShowStatusBehavior (uses above strategies)

     ===Followups===
     1. How to add new devices?
    Just compose new behaviors; no inheritance modifications needed.

    2. How to prevent object explosion?
    Because composition → avoids 2×2×2 subclasses.

    3. Can you add a “Low Power Mode”?
    Yes → Another BatteryBehavior or ShowBehavior.

    4. How would you test this?
    Mock BatteryBehavior + ChargingBehavior and assert show() output.

    5. What principle did you use?
    Interface Segregation
    Strategy Pattern
    Composition > Inheritance
    Open/Closed principle

*/

interface BatteryBehavior {
    String getBatteryStatus();  // "80%" or "No battery available"
    boolean hasBattery();
}

interface ChargingBehavior {
    boolean isCharging();
}

interface ShowBehavior {
    void show();
}

class HasBattery implements BatteryBehavior {
    private int percentage;

    public HasBattery(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String getBatteryStatus() {
        return percentage + "%";
    }

    @Override
    public boolean hasBattery() {
        return true;
    }

    public void setPercentage(int p) {
        this.percentage = p;
    }
}

class NoBattery implements BatteryBehavior {
    @Override
    public String getBatteryStatus() {
        return "No battery available";
    }

    @Override
    public boolean hasBattery() {
        return false;
    }
}

class Charging implements ChargingBehavior {
    @Override
    public boolean isCharging() {
        return true;
    }
}

class NotCharging implements ChargingBehavior {
    @Override
    public boolean isCharging() {
        return false;
    }
}

class DefaultShowBehavior implements ShowBehavior {

    private BatteryBehavior batteryBehavior;
    private ChargingBehavior chargingBehavior;

    public DefaultShowBehavior(BatteryBehavior bb, ChargingBehavior cb) {
        this.batteryBehavior = bb;
        this.chargingBehavior = cb;
    }

    @Override
    public void show() {
        boolean charging = chargingBehavior.isCharging();
        boolean hasBattery = batteryBehavior.hasBattery();
        String batteryStatus = batteryBehavior.getBatteryStatus();

        if (charging && hasBattery) {
            System.out.println("Charging, Battery at " + batteryStatus);
        } else if (charging && !hasBattery) {
            System.out.println("Charging, No battery available");
        } else if (!charging && hasBattery) {
            System.out.println("Battery at " + batteryStatus);
        } else {
            System.out.println("No battery available");
        }
    }
}

class AlexaDevice {

    private BatteryBehavior batteryBehavior;
    private ChargingBehavior chargingBehavior;
    private ShowBehavior showBehavior;

    public AlexaDevice(BatteryBehavior bb, ChargingBehavior cb) {
        this.batteryBehavior = bb;
        this.chargingBehavior = cb;
        this.showBehavior = new DefaultShowBehavior(bb, cb);
    }

    public void show() {
        showBehavior.show();
    }
}

public class AlexaDevices {
    public class Main {
        public static void main(String[] args) {

            AlexaDevice audioScreenBattery =
                    new AlexaDevice(new HasBattery(80), new Charging());
            audioScreenBattery.show();
            // Charging, Battery at 80%

            AlexaDevice screenOnly =
                    new AlexaDevice(new NoBattery(), new Charging());
            screenOnly.show();
            // Charging, No battery available

            AlexaDevice audioBattery =
                    new AlexaDevice(new HasBattery(55), new NotCharging());
            audioBattery.show();
            // Battery at 55%

            AlexaDevice audioScreen =
                    new AlexaDevice(new NoBattery(), new NotCharging());
            audioScreen.show();
            // No battery available
        }
    }

}
