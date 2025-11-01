import java.util.*;
import java.util.stream.Collectors;

// Ref : https://www.youtube.com/playlist?list=PLFGoYjJG_fqp52WKlmgF4cV72KS9_9tih
public class Streams {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 10, 20, 30, 15, 1);

        // Question 1: Sum of numbers
        Integer sum = list.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Sum: " + sum);

        // Question 2: average of numbers
        double avg = list.stream().mapToInt(Integer::intValue).average().orElse(0);
        System.out.println("Avg: " + avg);

        // Question 3 : square, filter, avg
        double avg1 = list
                .stream()
                .map((num) -> num * num)
                .filter((num) -> num > 100)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);

        System.out.println("Avg1: " + avg1);

        // Question 4 : Even, Odd
        List<Integer> evens = list
                .stream()
                .filter(n -> n % 2 == 0)
                .toList();

        System.out.println("Evens: " + evens);

        // Question 5 : Numbers starts with prefix 2
        List<Integer> ints2 = list
                .stream()
                .map(String::valueOf)
                .filter(e -> e.startsWith("2"))
                .map(Integer::valueOf)
                .toList();

        System.out.println("ints2: " + ints2);

        // Question 6 : Duplicates in a list
        Set<Integer> dups = list
                .stream()
                .filter(e -> Collections.frequency(list,e) > 1)
                .collect(Collectors.toSet());

        System.out.println("Dups: " + dups);

        // Question 7 : Max and min
        int max = list
                .stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);

        System.out.println("max: " + max);

        // Question 8: Sort
        List<Integer> sorted = list
                .stream()
                .sorted(Collections.reverseOrder())
                .toList();

        System.out.println("sorted: " + sorted);

        // Question 9 : Limit
        int limitSum = list
                .stream()
                .limit(5)
                .mapToInt(Integer::intValue)
                        .sum();

        System.out.println("limitSum: " + limitSum);

        // Question 10 : Second highest
        int secondH = list
                .stream()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .orElse(0);

        System.out.println("secondH: " + secondH);
    }
}
