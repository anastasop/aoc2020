import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class A {
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
        while (!p1.isEmpty() && !p2.isEmpty()) {
            var n1 = p1.removeFirst();
            var n2 = p2.removeFirst();
            if (n1 > n2) {
                p1.addLast(n1);
                p1.addLast(n2);
            } else {
                p2.addLast(n2);
                p2.addLast(n1);
            }
        }

        var winner = p1.isEmpty() ? p2 : p1;
        var score = 0L;
        var factor = winner.size();
        for (var elem: winner) {
            score += factor * elem;
            factor--;
        }
        System.out.println(score);
    }
}
