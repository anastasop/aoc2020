import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

public class B {
    public static void main(String[] args) throws Exception {
        var numbers = new HashMap<Integer, Number>();
        int round = 1;
        int previousNumber = -1;

        var reader = new BufferedReader(new InputStreamReader(System.in));
        for (var s = reader.readLine(); s != null; s = reader.readLine(), round++) {
            previousNumber = Integer.parseInt(s);
            var num = new Number();
            num.speak(round);
            numbers.put(previousNumber, num);
        }

        for(; round <= 30000000; round++) {
            var num = numbers.get(previousNumber);
            previousNumber = num.seenBefore() ? num.gap() : 0;

            num = numbers.get(previousNumber);
            if (num == null) {
                num = new Number();
                numbers.put(previousNumber, num);
            }
            num.speak(round);
        }

        System.out.println(previousNumber);
    }
}

class Number {
    private final int[] spoken = new int[2];

    void speak(int round) {
        spoken[0] = spoken[1];
        spoken[1] = round;
    }

    boolean seenBefore() {
        return spoken[0] > 0 && spoken[1] > 0;
    }

    int gap() {
        return spoken[1] - spoken[0];
    }
}
