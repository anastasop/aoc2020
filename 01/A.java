
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class A {
    public static void main(String[] args) {
        var numbers = new BufferedReader(new InputStreamReader(System.in))
                .lines()
                .mapToInt(Integer::parseInt)
                .toArray();
        Arrays.sort(numbers);

        int i = 0;
        int j = numbers.length - 1;
        int product = 0;
        while (i < j) {
            var sum = numbers[i] + numbers[j];
            if (sum == 2020) {
                product = numbers[i] * numbers[j];
                break;
            } else if (sum > 2020) {
                j--;
            } else {
                i++;
            }
        }

        if (product != 0) {
            System.out.printf("%d X %d = %d", numbers[i], numbers[j], product);
        }
    }
}
