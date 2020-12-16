import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class B {
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

        for (var ticket: tickets) {
            var values = ticket.getValues();
            for (int pos = 0; pos < values.size(); pos++) {
                int value = values.get(pos);
                var numValid = fields.stream().filter(f -> f.test(value)).count();
                if (numValid != 0) {
                    final int p = pos;
                    fields.stream().filter(f -> !f.test(value)).forEach(f -> f.addCant(p));
                }
            }
        }

        fields.sort(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o2.getCantPositions().size() - o1.getCantPositions().size();
            }
        });

        var candidates = new boolean[fields.size()];
        Arrays.fill(candidates, true);

        var thisCandidates = new boolean[fields.size()];
        for (var field: fields) {
            System.arraycopy(candidates, 0, thisCandidates, 0, candidates.length);
            for (int pos: field.getCantPositions()) {
                thisCandidates[pos] = false;
            }

            int sel = -1;
            int numSels = 0;
            for (int pos = 0; pos < thisCandidates.length; pos++) {
                if (thisCandidates[pos]) {
                    if (sel < 0) {
                        sel = pos;
                    }
                    numSels++;
                }
            }
            if (numSels > 1 || numSels == 0) {
                throw new IllegalArgumentException(field.toString());
            }
            candidates[sel] = false;
            field.setPosition(sel);
        }

        final var myTicket = tickets.get(0);
        var product = fields.stream()
                .filter(f -> f.getName().startsWith("departure"))
                .mapToLong(field -> myTicket.getValues().get(field.getPosition()))
                .reduce(1, (a,b) -> a * b);
        System.out.println(product);
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
    private final Set<Integer> cantPositions = new HashSet<>();
    private int position = -1;

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

    public void addCant(int pos) {
        cantPositions.add(pos);
    }

    public Set<Integer> getCantPositions() {
        return cantPositions;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public int getPosition() {
        return this.position;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", name, ranges.stream().map(Range::toString).collect(Collectors.joining(" or ")));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Field && name.equals(((Field)other).name);
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
