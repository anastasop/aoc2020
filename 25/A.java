import java.io.BufferedReader;
import java.io.InputStreamReader;

public class A {
    public static final long MAGIC = 20201227;

    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var cardPublicKey = Long.parseLong(reader.readLine());
        var doorPublicKey = Long.parseLong(reader.readLine());

        var cardLoopSize = guessLoopSize(7, cardPublicKey);
        var doorLoopSize = guessLoopSize(7, doorPublicKey);
        System.out.println(cardLoopSize);
        System.out.println(doorLoopSize);
        System.out.println(transform(doorPublicKey, cardLoopSize));
        System.out.println(transform(cardPublicKey, doorLoopSize));
    }

    public static long transform(final long subject, final int loopSize) {
        long val = 1;
        for (int i = 0; i < loopSize; i++) {
            val = (val * subject) % MAGIC;
        }
        return val;
    }

    public static int guessLoopSize(final long subject, final long key) {
        int num = 0;
        long val = 1;
        for(;;) {
            if (val == key) {
                return num;
            }
            val = (val * subject) % MAGIC;
            num++;
        }
    }
}
