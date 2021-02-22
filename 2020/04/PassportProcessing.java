import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Passport {
    private static final List<String> REQUIRED_FIELDS = List.of("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

    private static final List<String> ALLOWED_EYE_COLORS = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static boolean intInRange(String value, int minInclusive, int maxInclusive) {
        int intValue = Integer.parseInt(value.replaceAll("[\\D]", ""));
        return minInclusive <= intValue && intValue <= maxInclusive;
    }

    private static final Map<String, Predicate<String>> FIELD_CONSTRAINTS = Map.ofEntries(
            new SimpleEntry<>("byr", v -> v.length() == 4 && intInRange(v, 1920, 2002)),
            new SimpleEntry<>("iyr", v -> v.length() == 4 && intInRange(v, 2010, 2020)),
            new SimpleEntry<>("eyr", v -> v.length() == 4 && intInRange(v, 2020, 2030)),
            new SimpleEntry<>("hgt", v -> v.contains("cm") ? intInRange(v, 150, 193)
                                                           : intInRange(v, 59, 76)),
            new SimpleEntry<>("hcl", v -> v.matches("#[0-9a-f]{6}")),
            new SimpleEntry<>("ecl", ALLOWED_EYE_COLORS::contains),
            new SimpleEntry<>("pid", v -> v.matches("[0-9]{9}"))
    );

    private final Map<String, String> fields;

    public Passport(String entry) {
        fields = new HashMap<>();

        for (String field : entry.split("\\s+")) {
            String[] parts = field.split(":");
            fields.put(parts[0], parts[1]);
        }
    }

    public boolean hasRequiredFields() {
        return REQUIRED_FIELDS.stream().allMatch(fields::containsKey);
    }

    public boolean isValid() {
        return hasRequiredFields() &&
               fields.entrySet().stream().allMatch(e -> {
                   Predicate<String> constraint = FIELD_CONSTRAINTS.get(e.getKey());
                   return constraint == null || constraint.test(e.getValue());
               });
    }
}

public class PassportProcessing {
    private static List<Passport> readEntries(String filename) throws IOException {
        String fileContents = Files.readString(Paths.get(filename));
        return Arrays.stream(fileContents.split("\\n{2,}")).map(Passport::new).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Passport> passports = readEntries(filename);
            System.out.println("Part 1: " + passports.stream().filter(Passport::hasRequiredFields).count());
            System.out.println("Part 2: " + passports.stream().filter(Passport::isValid).count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
