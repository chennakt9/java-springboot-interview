import java.util.*;
import java.util.stream.Collectors;

public class Streams {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        // 1. Filter: only even numbers
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        System.out.println("Even numbers: " + evens);

        // 2. Map: square each number
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .toList();
        System.out.println("Squares: " + squares);

        // 3. Sum: sum of all numbers
        int sum = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);
    }
}
