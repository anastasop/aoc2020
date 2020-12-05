import java.io.BufferedReader;
import java.io.InputStreamReader;

public class A {
    public static void main(String[] args) {
        var max = new BufferedReader(new InputStreamReader(System.in)).lines().mapToInt(A::seatPosition).max().getAsInt();
        System.out.println(max);
    }

    public static int seatPosition(String s) {
        int id = 0;

        for (var i = 0; i < 10; i++) {
            id *= 2;
            if (s.charAt(i) == 'B' || s.charAt(i) == 'R') {
                id++;
            }
        }

        return id;
    }
}
