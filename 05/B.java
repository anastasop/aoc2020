import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class B {
    public static void main(String[] args) {
        var ids = new BufferedReader(new InputStreamReader(System.in)).lines().mapToInt(B::seatPosition).toArray();

        Arrays.sort(ids);
        for (int i = 0, v = ids[0]; i < ids.length; i++, v++) {
            if (v != ids[i]) {
                System.out.println("My seat: " + v);
                break;
            }
        }
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
