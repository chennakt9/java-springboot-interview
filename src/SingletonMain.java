public class SingletonMain {
    public static void main(String[] args) {
        // Call getInstance multiple times
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        // Verify they point to the same object
        System.out.println("s1 == s2 ? " + (s1 == s2));
    }
}
