import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class A {
    private static final int WINDOW_SIZE = 25;

    public static void main(String[] args) {
        var numbers = new ArrayList<Long>();

        new BufferedReader(new InputStreamReader(System.in))
                .lines()
                .mapToLong(Long::parseLong)
                .forEach(numbers::add);

        for (int i = WINDOW_SIZE; i < numbers.size(); i++) {
            var number = numbers.get(i);
            if (!isSum(number, numbers.subList(i - WINDOW_SIZE, i))) {
                System.out.println(number);
            }
        }
    }

    public static boolean isSum(Long number, List<Long> numbers) {
        for (var a: numbers) {
            for (var b: numbers) {
                if (a + b == number && !a.equals(b)) {
                    return true;
                }
            }
        }

        return false;
    }
}
