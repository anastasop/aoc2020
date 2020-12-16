import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) throws Exception {
        var reader = new BufferedReader(new InputStreamReader(System.in));

        List<Ticket> tickets = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            if (line.startsWith("nearby tickets") || line.startsWith("your ticket")) {
                continue;
            }

            if (line.contains(",")) {
                tickets.add(new Ticket(Arrays.stream(line.split(",")).map(Integer::parseInt).collect(Collectors.toList())));
            } else if (line.contains(":")) {
                var toks = line.split(":");
                var name = toks[0];
                var ranges = Arrays.stream(toks[1].split("\\s+"))
                        .filter(s -> Pattern.matches("\\d+-\\d+", s))
                        .map(s -> {
                            var nums = s.split("-");
                            return new Range(Integer.parseInt(nums[0]), Integer.parseInt(nums[1]));
                        }).collect(Collectors.toList());
                fields.add(new Field(name, ranges));
            }
        }

        int sum = 0;
        for (var ticket: tickets.subList(1, tickets.size())) {
            for (var value: ticket.getValues()) {
                var numValid = fields.stream().filter(f -> f.test(value)).count();
                if (numValid == 0) {
                    sum += value;
                }
            }
        }

        System.out.println(sum);
    }
}

class Range implements Predicate<Integer> {
    private final int low;
    private final int high;

    public Range(int low, int high) {
        this.low = low;
        this.high = high;

    }

    @Override
    public boolean test(Integer val) {
        return low <= val && val <= high;
    }

    @Override
    public String toString() {
        return String.format("%d-%d", low, high);
    }
}

class Field implements Predicate<Integer> {
    private final String name;
    private final List<Range> ranges;

    public Field(String name, List<Range> ranges) {
        this.name = name;
        this.ranges = new ArrayList<>(ranges);
    }

    @Override
    public boolean test(Integer val) {
        for (var range: ranges) {
            if (range.test(val)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, ranges.stream().map(Range::toString).collect(Collectors.joining(" or ")));
    }
}

class Ticket {
    private final List<Integer> values;

    public Ticket(List<Integer> values) {
        this.values = new ArrayList<>(values);
    }

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return values.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}
