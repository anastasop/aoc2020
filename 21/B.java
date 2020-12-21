import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class B {
    public static void main(String[] args) {
        final var foods = new BufferedReader(new InputStreamReader(System.in)).lines().map(Food::new).collect(Collectors.toList());

        final var allIngredients = new HashSet<String>();
        foods.forEach(food -> allIngredients.addAll(food.ingredients));

        final var pairingCandidates = new HashMap<String, HashSet<String>>();
        for (var food: foods) {
            for (var allergen: food.allergens) {
                var prev = pairingCandidates.get(allergen);
                if (prev == null) {
                    pairingCandidates.put(allergen, new HashSet<>(food.ingredients));
                } else {
                    prev.retainAll(food.ingredients);
                }
            }
        }

        var inert = new HashSet<String>(allIngredients);
        for (var c: pairingCandidates.values()) {
            inert.removeAll(c);
        }
        pairingCandidates.forEach((k, v) -> v.removeAll(inert));

        final var pairings = new ArrayList<Pair>();
        final AtomicInteger assignments = new AtomicInteger(1);

        while (assignments.get() > 0) {
            assignments.set(0);
            pairingCandidates.forEach((allergen, ingredients) -> {
                if (ingredients.size() == 1) {
                    var ingredient = ingredients.iterator().next();
                    pairings.add(new Pair(ingredient, allergen));
                    assignments.addAndGet(1);
                    pairingCandidates.forEach((k, v) -> v.remove(ingredient));
                }
            });
        }

        pairings.sort(Comparator.comparing(p -> p.allergen));
        pairings.forEach(System.out::println);
        System.out.println(pairings.stream().map(p -> p.ingredient).collect(Collectors.joining(",")));
    }
}

class Food {
    public List<String> ingredients = new ArrayList<>();
    public List<String> allergens = new ArrayList<>();

    public Food(String list) {
        var matcher = Pattern.compile("[a-z]+").matcher(list);
        var section = ingredients;
        while (matcher.find()) {
            String s = matcher.group();
            if (s.equals("contains")) {
                section = allergens;
                continue;
            }
            section.add(s);
        }
    }

    public String toString() {
        var sb = new StringBuilder();
        for (String s: ingredients) {
            sb.append(s);
            sb.append(" ");
        }
        sb.append("(contains ");
        sb.append(String.join(", ", allergens));
        sb.append(")\n");
        return sb.toString();
    }
}
class Pair {
    public final String ingredient;
    public final String allergen;

    public Pair(String ingredient, String allergen) {
        this.ingredient = ingredient;
        this.allergen = allergen;
    }

    public String toString() {
        return String.format("%s contains %s", ingredient, allergen);
    }
}
