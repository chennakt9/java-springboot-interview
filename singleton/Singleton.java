/*
    ===All approaches of singleton===
    Eager Initialization – simple, thread-safe, not lazy.
    Static Holder (Bill Pugh) – lazy, thread-safe, needs extra care for reflection/cloning/serialization.
    Double-Checked Locking with volatile – lazy, thread-safe, a bit verbose.
    Enum Singleton – lazy or eager depending on JVM, completely bulletproof.
*/

//public class Singleton {
//
//    private static volatile Singleton instance;
//
//    private Singleton() {
//        System.out.println("Singleton instance created!");
//    }
//
//    public static Singleton getInstance() {
//        if (instance == null) {
//            synchronized (Singleton.class) {
//                if (instance == null) {
//                    instance = new Singleton();
//                }
//            }
//        }
//        return instance;
//    }
//}

import java.io.Serializable;

public class Singleton implements Serializable, Cloneable {

    // Private constructor
    private Singleton() {
        // Prevent Reflection
        if (Holder.INSTANCE != null) {
            throw new RuntimeException("Singleton already created!");
        }
        System.out.println("Singleton instance created!");
    }

    // Static Holder class for lazy-loading
    private static class Holder {
        private static final Singleton INSTANCE = new Singleton();
    }

    // Global access point
    public static Singleton getInstance() {
        return Holder.INSTANCE;
    }

    // Prevent Cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }

    // Prevent Serialization from creating a new instance
    protected Object readResolve() {
        return getInstance();
    }

    // Example method
    public void doSomething() {
        System.out.println("Using singleton: " + this);
    }
}
