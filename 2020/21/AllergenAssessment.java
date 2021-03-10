import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Food {
    private final List<String> ingredients, allergens;

    public Food(String line) {
        String[] parts = line.split(" \\(contains ");
        ingredients = Arrays.stream(parts[0].split(" ")).collect(Collectors.toList());
        allergens = Arrays.stream(parts[1].split("\\)")[0].split(", ")).collect(Collectors.toList());
    }

    public Food(Food other) {
        ingredients = new ArrayList<>(other.ingredients);
        allergens = new ArrayList<>(other.allergens);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return ingredients.equals(food.ingredients) && allergens.equals(food.allergens);
    }

    @Override
    public int hashCode() { return Objects.hash(ingredients, allergens); }

    public List<String> getAllergens() { return allergens; }
    public List<String> getIngredients() { return ingredients; }

    public void intersect(Food other) {
        ingredients.retainAll(other.ingredients);
        allergens.retainAll(other.allergens);
    }
}

public class AllergenAssessment {
    private static List<Food> readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(Food::new).collect(Collectors.toList());
        }
    }

    private static Map<String, List<String>> getRoughMappings(List<Food> foods) {
        return foods.stream().map(food -> {
            Food copy = new Food(food);
            for (Food f : foods) {
                // Intersect ingredient and allergen sets if there are common allergens
                Set<String> allergens = new HashSet<>(f.getAllergens());
                allergens.retainAll(copy.getAllergens());
                if (!allergens.isEmpty()) {
                    copy.intersect(f);
                }
            }
            Collections.sort(copy.getIngredients());
            return copy;
        }).distinct().collect(Collectors.toMap(f -> f.getAllergens().get(0), Food::getIngredients));
    }

    private static Map<String, String> getUniqueMappings(List<Food> foods) {
        Map<String, List<String>> allergenToIngredients = getRoughMappings(foods);
        List<String> orderedKeys = new ArrayList<>(allergenToIngredients.keySet());

        for (int i = 0; i < orderedKeys.size(); ++i) {
            orderedKeys.sort(Comparator.comparing(k -> allergenToIngredients.get(k).size()));

            if (allergenToIngredients.get(orderedKeys.get(i)).size() != 1) {
                throw new AssertionError("ingredient set not equal to 1");
            } else {
                String uniqueMatch = allergenToIngredients.get(orderedKeys.get(i)).get(0);
                for (int j = i + 1; j < orderedKeys.size(); ++j) {
                    allergenToIngredients.get(orderedKeys.get(j)).remove(uniqueMatch);
                }
            }
        }

        return allergenToIngredients.entrySet().stream()
                                    .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().get(0)));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Food> foods = readLines(filename);
            Map<String, String> mappings = getUniqueMappings(foods);

            System.out.println("Part 1: " + foods.stream().flatMap(f -> f.getIngredients().stream())
                                                          .filter(i -> !mappings.containsValue(i)).count());
            System.out.println("Part 2: " + mappings.entrySet().stream().sorted(Entry.comparingByKey())
                                                    .map(Entry::getValue).collect(Collectors.joining(",")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
