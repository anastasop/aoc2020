import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class B {
    public static void main(String[] args) {
        List<Integer> rankings = new BufferedReader(new InputStreamReader(System.in)).lines()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        rankings.add(rankings.stream().max(Integer::compareTo).orElseGet(() -> 0) + 3);
        rankings.sort(Integer::compareTo);

        var counts = new HashMap<Long, Long>();
        var times = count(0, rankings.toArray(new Integer[0]), 0, counts);
        System.out.println(times);
    }

    public static long count(int currLevel, Integer[] rankings, int pos, Map<Long, Long> counts) {
        var thisKey = (long)pos << 32 | (long)currLevel;

        var thisTimes = counts.get(thisKey);
        if (thisTimes != null) {
            return thisTimes;
        }

        if (pos == rankings.length - 1) {
            counts.put(thisKey, 1L);
            return 1L;
        }

        long counter = 0;
        while (pos < rankings.length) {
            var rank = rankings[pos];
            if (rank - currLevel > 3) {
                break;
            }
            pos++;
            counter += count(rank, rankings, pos, counts);
        }

        counts.put(thisKey, counter);
        return counter;
    }
}
