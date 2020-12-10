import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) {
        List<Integer> rankings = new BufferedReader(new InputStreamReader(System.in)).lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        rankings.add(rankings.stream().max(Integer::compareTo).orElseGet(() -> 0) + 3);
        rankings.sort(Integer::compareTo);

        var level = 0;
        var diffs = new int[4];
        for (var rank: rankings) {
            var diff = rank - level;
            if (diff > 3) {
                break;
            }
            diffs[diff]++;
            level = rank;
        }

        for (var i = 0; i < diffs.length; i++) {
            System.out.printf("%d: %d%n", i, diffs[i]);
        }
        System.out.println(diffs[1] * diffs[3]);
    }
}
