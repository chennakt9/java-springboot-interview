package designpatterns;

public class Computer {
    // required fields
    private final String cpu;
    private final Integer ramGB;

    // optional fields
    private final String storageType;
    private final boolean hasGraphicsCard;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ramGB = builder.ramGB;
        this.storageType = builder.storageType;
        this.hasGraphicsCard = builder.hasGraphicsCard;
    }

    public String getCpu() {
        return cpu;
    }

    public Integer getRamGB() {
        return ramGB;
    }

    public String getStorageType() {
        return storageType;
    }

    public boolean isHasGraphicsCard() {
        return hasGraphicsCard;
    }

    public static class Builder {
        // required fields
        private final String cpu;
        private final Integer ramGB;

        // optional fields
        private String storageType;
        private boolean hasGraphicsCard;

        public Builder(String cpu, int ramGB) {
            this.cpu = cpu;
            this.ramGB = ramGB;
        }

        public Builder storageType(String storageType) {
            this.storageType = storageType;

            return this;
        }

        public Builder hasGraphicsCard(boolean hasGraphicsCard) {
            this.hasGraphicsCard = hasGraphicsCard;

            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }

    public static void main(String[] args) {

        Computer myPc = new Computer.Builder("Intel i7", 16)
                .storageType("SSD")
                .hasGraphicsCard(true)
                .build();

        // printing output
        System.out.println("CPU: " + myPc.getCpu());
        System.out.println("RAM: " + myPc.getRamGB() + " GB");
        System.out.println("Storage: " + myPc.getStorageType());
        System.out.println("Graphics Card: " + myPc.isHasGraphicsCard());
    }
}
