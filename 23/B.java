import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class B {
    public static void main(String[] args) throws Exception {
        final var line = new BufferedReader(new FileReader("./Ainput.txt")).readLine();

        Supplier<Stream<Integer>> nums = () -> Arrays.stream(line.split("")).map(Integer::parseInt);
        var max = nums.get().max(Integer::compareTo).orElseThrow();
        var numbers = Stream.concat(nums.get(), Stream.iterate(max + 1, (t) -> t + 1));
        var million = numbers.limit(1000000);

        var cups = new Cups(million);
        for (int i = 0; i < 10000000; i++) {
            cups.move();
        }
        long i1 = cups.find(1).next.value;
        long i2 = cups.find(1).next.next.value;
        System.out.println(i1 * i2);
    }
}

class Cups {
    static class Cup {
        Cup prev;
        Cup next;
        int value;
    }

    Cup curr;
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    Map<Integer, Cup> positions = new HashMap<>();

    public Cups(Stream<Integer> values) {
        final var cup = new AtomicReference<Cup>();
        final var prev = new AtomicReference<Cup>();

        values.forEach(i -> {
            cup.set(new Cup());
            cup.get().value = i;
            positions.put(i, cup.get());
            if (curr == null) {
                curr = cup.get();
            }

            cup.get().prev = prev.get();
            cup.get().next = null;
            if (prev.get() != null) {
                prev.get().next = cup.get();
            }

            min = Math.min(min, i);
            max = Math.max(max, i);

            prev.set(cup.get());
        });

        if (cup.get() != null) {
            cup.get().next = curr;
        }
    }

    public void move() {
        var cutFirst = curr.next;
        var cutLast = cutFirst.next.next;

        curr.next = cutLast.next;
        cutLast.next.prev = curr;

        int destNum = -1;
        int c0 = cutFirst.value;
        int c1 = cutFirst.next.value;
        int c2 = cutFirst.next.next.value;

        for (int i = curr.value - 1; i >= min; i--) {
            if (c0 != i && c1 != i && c2 != i) {
                destNum = i;
                break;
            }
        }
        if (destNum < 0) {
            for (int i = max; i >= curr.value; i--) {
                if (c0 != i && c1 != i && c2 != i) {
                    destNum = i;
                    break;
                }
            }
        }

        Cup dest = find(destNum);
        cutFirst.prev = dest;
        cutLast.next = dest.next;
        dest.next.prev = cutLast;
        dest.next = cutFirst;

        curr = curr.next;
    }

    public Cup find(int v) {
        return positions.get(v);
    }
}
