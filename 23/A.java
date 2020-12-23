import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) throws Exception {
        var line = new BufferedReader(new InputStreamReader(System.in)).readLine();

        var cups = new Cups(Arrays.stream(line.split("")).map(Integer::parseInt).collect(Collectors.toList()));

        for (int i = 0; i < 100; i++) {
            cups.move();
        }
        System.out.println(cups);
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

    public Cups(Collection<? extends Integer> values) {
        Cup prev = null;
        Cup cup = null;

        for (Integer i: values) {
            cup = new Cup();
            cup.value = i;
            if (curr == null) {
                curr = cup;
            }

            cup.prev = prev;
            cup.next = null;
            if (prev != null) {
                prev.next = cup;
            }

            min = Math.min(min, i);
            max = Math.max(max, i);

            prev = cup;
        }

        if (cup != null) {
            cup.next = curr;
        }
    }

    public void move() {
        var cutFirst = curr.next;
        var cutLast = cutFirst.next.next;

        curr.next = cutLast.next;
        cutLast.next.prev = curr;

        Cup dest = null;
        for (int i = curr.value - 1; dest == null && i >= min; i--) {
            dest = find(i);
        }
        for (int i = max; dest == null && i >= curr.value; i--) {
            dest = find(i);
        }

        cutFirst.prev = dest;
        cutLast.next = dest.next;
        dest.next.prev = cutLast;
        dest.next = cutFirst;
        cutFirst.prev = dest;

        curr = curr.next;
    }

    private Cup find(int v) {
        var it = iterator();
        while (it.hasNext()) {
            var c = it.next();
            if (c.value == v) {
                return c;
            }
        }
        return null;
    }

    private Iterator iterator() {
        return new Iterator(curr);
    }

    static class Iterator implements java.util.Iterator<Cup> {
        Cup start;
        Cup current;

        Iterator(Cup start) {
            this.start = start;
            this.current = null;
        }

        @Override
        public boolean hasNext() {
            return current == null || current != start;
        }

        @Override
        public Cup next() {
            if (current == null) {
                current = start;
            }
            Cup c = current;
            current = current.next;
            return c;
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        var it = iterator();
        while (it.hasNext()) {
            var c = it.next();
            sb.append((char)('0' + c.value));
        }
        return sb.toString();
    }
}