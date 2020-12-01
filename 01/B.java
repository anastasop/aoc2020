
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class B {
    public static void main(String[] args) {
        var numbers = new BufferedReader(new InputStreamReader(System.in))
                .lines()
                .mapToInt(Integer::parseInt)
                .toArray();

        int i = 0, j = 0, k = 0;
        int product = 0;

        loops:
        for (i = 0; i < numbers.length; i++) {
            for (j = i + 1; j < numbers.length; j++) {
                for (k = j + 1; k < numbers.length; k++) {
                    var sum = numbers[i] + numbers[j] + numbers[k];
                    if (sum == 2020) {
                        product = numbers[i] * numbers[j] * numbers[k];
                        break loops;
                    }
                }
            }
        }

        if (product != 0) {
            System.out.printf("%d X %d X %d = %d", numbers[i], numbers[k], numbers[j], product);
        }
    }
}
