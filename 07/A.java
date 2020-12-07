import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class A {
    public static void main(String[] args) throws Exception {
        var rules = new Rules();
        new BufferedReader(new InputStreamReader(System.in)).lines().forEach(rules::addRule);

        System.out.println(rules.countDistinctContainers("shiny gold"));
    }
}

class Rules {
    private final Pattern rule = Pattern.compile("(\\d+)\\s+([a-z\\s]+)\\s+(bag|bags)");
    private final Map<String, Bag> bags = new HashMap<>();

    public void addRule(String line) {
        var matcher = rule.matcher("1 " + line.replace("no other bag", ""));

        var rules = new ArrayList<BagAndQuantity>();
        while (matcher.find()) {
            int num = Integer.parseInt(matcher.group(1));
            String color = matcher.group(2).strip();

            var bag = bags.computeIfAbsent(color, Bag::new);
            rules.add(new BagAndQuantity(bag, num));
        }

        var container = rules.get(0).getBag();
        rules.stream().skip(1).forEach(container::include);
    }

    public int countDistinctContainers(String color) {
        var bag = bags.get(color);
        if (bag == null) {
            throw new IllegalArgumentException("No bag of color " + color);
        }

        var counter = new Counter();
        countDistinctContainers(bag, counter);
        return counter.value();
    }

    public void countDistinctContainers(Bag bag, Counter counter) {
        bag.visit();

        for (var other: bag.getContainers()) {
            if (!other.isVisited()) {
                counter.increase();
                countDistinctContainers(other, counter);
            }
        }
    }
}

class Bag {
    private final String color;
    private final List<Bag> containers = new ArrayList<>();
    private final List<BagAndQuantity> content = new ArrayList<>();
    private boolean visited = false;

    public Bag(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void include(BagAndQuantity bq) {
        bq.getBag().containers.add(this);
        content.add(bq);
    }

    public void visit() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public List<Bag> getContainers() {
        return containers;
    }

    public List<BagAndQuantity> getContent() {
        return content;
    }
}

class BagAndQuantity {
    private final Bag bag;
    private final int quantity;

    public BagAndQuantity(Bag bag, int quantity) {
        this.bag = bag;
        this.quantity = quantity;
    }

    public Bag getBag() {
        return bag;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Counter {
    private int count = 0;

    void increase() {
        count++;
    }

    int value() {
        return count;
    }
}