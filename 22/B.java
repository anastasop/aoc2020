import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class B {
    public static void main(String[] args) throws Exception {
        final var decks = new ArrayList<ArrayDeque<Integer>>();
        decks.add(new ArrayDeque<Integer>());
        decks.add(new ArrayDeque<Integer>());
        decks.add(new ArrayDeque<Integer>());
        var index = 1;
        var reader = new BufferedReader(new InputStreamReader(System.in));
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("Player ")) {
                index = Integer.parseInt(line.substring("Player ".length(), line.length() - 1));
            } else if (!line.isEmpty()){
                decks.get(index).add(Integer.parseInt(line));
            }
        }

        var p1 = decks.get(1);
        var p2 = decks.get(2);

        var winner = game(new HashSet<Integer>(), p1, new HashSet<Integer>(), p2) ? p1 : p2;
        System.out.println(score(winner));
    }

    public static boolean game(HashSet<Integer> s1, Deque<Integer> p1, HashSet<Integer> s2, Deque<Integer> p2) {
        while (!p1.isEmpty() && !p2.isEmpty()) {
            var h1 = hash(p1);
            var h2 = hash(p2);
            if (s1.contains(h1) || s2.contains(h2)) {
                return true;
            } else {
                s1.add(h1);
                s2.add(h2);
            }

            var n1 = p1.removeFirst();
            var n2 = p2.removeFirst();

            boolean won1;
            if (p1.size() >= n1 && p2.size() >= n2) {
                won1 = game(
                        new HashSet<>(), new ArrayDeque<>(new ArrayList<>(p1).subList(0, n1)),
                        new HashSet<>(), new ArrayDeque<>(new ArrayList<>(p2).subList(0, n2))
                );
            } else {
                won1 = n1 > n2;
            }

            if (won1) {
                p1.addLast(n1);
                p1.addLast(n2);
            } else {
                p2.addLast(n2);
                p2.addLast(n1);
            }
        }

        return !p1.isEmpty();
    }

    public static int hash(Deque<Integer> deck) {
        int h = 0;
        for (int v: deck) {
            h = 31 * h + (v & 0xff);
        }
        return h;
    }

    public static long score(Deque<Integer> deck) {
        var score = 0L;
        var factor = deck.size();
        for (var elem: deck) {
            score += factor-- * elem;
        }
        return score;
    }
}
