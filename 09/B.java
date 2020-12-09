import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class B {
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
                var nums = cset(number, numbers);
                System.out.printf("%d %s%n", number, nums);
                nums.sort(Long::compareTo);
                System.out.println(nums.get(0) + nums.get(nums.size() - 1));
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

    public static List<Long> cset(Long number, List<Long> numbers) {
        int i, j;
        long sum;

        for (i = 0; i < numbers.size(); i++) {
            sum = numbers.get(i);
            for (j = i + 1; j < numbers.size(); j++) {
                sum += numbers.get(j);
                if (sum == number) {
                    return numbers.subList(i, j + 1);
                } else if (sum > number) {
                    break;
                }
            }
        }

        return Collections.emptyList();
    }
}
