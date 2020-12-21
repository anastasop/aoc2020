import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) {
        final var foods = new BufferedReader(new InputStreamReader(System.in)).lines().map(Food::new).collect(Collectors.toList());

        final var allIngredients = new HashSet<String>();
        foods.forEach(food -> allIngredients.addAll(food.ingredients));

        final var inertCandidates = new HashMap<String, HashSet<String>>();
        for (var food: foods) {
            for (var allergen: food.allergens) {
                var prev = inertCandidates.get(allergen);
                if (prev == null) {
                    inertCandidates.put(allergen, new HashSet<>(food.ingredients));
                } else {
                    prev.retainAll(food.ingredients);
                }
            }
        }

        var inert = new HashSet<String>(allIngredients);
        for (var c: inertCandidates.values()) {
            inert.removeAll(c);
        }

        int count = 0;
        for (var ingredient: inert) {
            for (var food: foods) {
                if (food.ingredients.contains(ingredient)) {
                    count++;
                }
            }
        }
        System.out.println(count);
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
