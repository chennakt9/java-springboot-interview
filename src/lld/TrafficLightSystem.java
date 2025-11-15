package lld;
import java.util.*;

/*
    1. You have a couple of traffic lights
    2. System receives real-time traffic data (like number of cars waiting per lane).
    3. Based on this data, system dynamically adjusts green light duration.
    4. Sensor-based adjustments.

    TrafficLightController
        - sensors
        - strategy
        - lights
        + adjustTimings()
        + controlCycle()

    Sensor
        - id
        + getTrafficData()

    ITimingStrategy
        + calculateDuration(data)

    DensityBasedStrategy
        + calculateDuration(data)

    FixedTimeStrategy
        + calculateDuration(data)

    TrafficLight
        - id
        - state
        - duration
        + changeState(LightState)
        + setDuration(int)

    TrafficData
        - vehicleCount
        + getVehicleCount

    Design patterns used:
    1. Strategy
*/

enum LightState {
    RED, YELLOW, GREEN
}

class TrafficData {
    private final int vehicleCount;

    public TrafficData(int vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }
}

class Sensor {

    Sensor(String id) {
    }

    public TrafficData getTrafficData() {
        int vehicles = (int)(Math.random() * 50);

        return new TrafficData(vehicles);
    }
}

class TrafficLight {
    private final String id;
    private int duration;
    private LightState state;
    TrafficLight(String id) {
        this.id = id;
        this.state = LightState.RED;
    }

    public void changeState(LightState newState) {
        this.state = newState;

        System.out.println("[Light " + id + "] changed to " + newState);

    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public LightState getState() {
        return state;
    }
}

class TrafficController {
    private final List<TrafficLight> trafficLights;
    private final List<Sensor> sensors;
    private final ITimeStrategy timeStrategy;
    TrafficController(List<TrafficLight> trafficLights, List<Sensor> sensors, ITimeStrategy timeStrategy) {
        this.trafficLights = trafficLights;
        this.sensors = sensors;
        this.timeStrategy = timeStrategy;
    }

    public void adjustTimings() {
        for (int i = 0; i < trafficLights.size(); i++) {
            TrafficData trafficData = sensors.get(i).getTrafficData();
            int duration = timeStrategy.calculateDuration(trafficData);
            trafficLights.get(i).setDuration(duration);
            System.out.println("Light " + trafficLights.get(i) + " set to " + duration + "s (vehicles: " + trafficData.getVehicleCount() + ")");
        }
    }

    public void controlCycle() throws InterruptedException {
        System.out.println("\n--- Running Traffic Cycle ---");
        for (TrafficLight light : trafficLights) {
            light.changeState(LightState.GREEN);
            Thread.sleep(light.getDuration() * 100L);
            light.changeState(LightState.YELLOW);
            Thread.sleep(100L);
            light.changeState(LightState.RED);
        }
    }
}

interface ITimeStrategy {
    int calculateDuration(TrafficData trafficData);
}

class DynamicDensityStrategy implements ITimeStrategy {
    private static final int BASE_DURATION = 20;

    @Override
    public int calculateDuration(TrafficData trafficData) {
        int vehicles = trafficData.getVehicleCount();

        return Math.min(90, BASE_DURATION + vehicles / 2); // some random algo
    }
}

public class TrafficLightSystem {
    public static void main(String[] args) throws InterruptedException {
        List<TrafficLight> lights = Arrays.asList(
                new TrafficLight("A"),
                new TrafficLight("B"),
                new TrafficLight("C")
        );

        List<Sensor> sensors = Arrays.asList(
                new Sensor("S1"),
                new Sensor("S2"),
                new Sensor("S3")
        );

        ITimeStrategy strategy = new DynamicDensityStrategy();

        TrafficController controller = new TrafficController(lights, sensors, strategy);

        while (true) {
            controller.adjustTimings();
            controller.controlCycle();

            System.out.println("\nCycle complete. Re-evaluating traffic data...\n");
            Thread.sleep(1000);
        }
    }
}


