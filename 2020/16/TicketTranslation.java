import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TicketTranslation {
    private static Map<String, int[][]> fieldRules;
    private static int[] myTicket;
    private static int[][] nearbyTickets;

    private static Stream<int[]> getTicketStream(String paragraph) {
        return paragraph.lines().skip(1).map(l -> Arrays.stream(l.split(","))
                                                        .mapToInt(Integer::parseInt).toArray());
    }

    private static void readInput(String filename) throws IOException {
        String[] fileContents = Files.readString(Paths.get(filename)).split("\\n{2,}");

        fieldRules = fileContents[0].lines().map(s -> s.split(": ")).collect(Collectors.toMap(
            l -> l[0], l -> Arrays.stream(l[1].split(" or "))
                                  .map(r -> Arrays.stream(r.split("-")).mapToInt(Integer::parseInt).toArray())
                                  .toArray(int[][]::new)));

        myTicket = getTicketStream(fileContents[1]).findFirst().get();
        nearbyTickets = getTicketStream(fileContents[2]).toArray(int[][]::new);
    }

    private static boolean fieldInRange(int[] range, int field) {
        // range[0] is the lower bound, range[1] is the upper bound
        return (range[0] <= field && field <= range[1]); }

    private static int getSingleErrorRate(int field, List<int[]> rules) {
        // use the special value `-1` if no error is found
        return rules.stream().anyMatch(range -> fieldInRange(range, field)) ? -1 : field;
    }

    private static IntStream getErrorRates() {
        List<int[]> allRules = fieldRules.values().stream().flatMap(Arrays::stream)
                                                           .sorted(Comparator.comparingInt(rule -> rule[1]))
                                                           .collect(Collectors.toList());
        for (int i = allRules.size() - 1; i > 0; --i) {
            int[] lastRule = allRules.get(i), secondLastRule = allRules.get(i - 1);
            if (secondLastRule[1] <= lastRule[1] && secondLastRule[1] >= lastRule[0] - 1) {
                allRules.set(i - 1, new int[] { secondLastRule[0], lastRule[1] });
                allRules.remove(i--);
            }
        }

        return Arrays.stream(nearbyTickets)
                     .mapToInt(fields -> Arrays.stream(fields).map(field -> getSingleErrorRate(field, allRules))
                                                              .max().orElse(-1));
    }

    private static int[][] transposeArray(int[][] input) {
        if (input.length == 0) { throw new IllegalArgumentException("The array must not be empty"); }

        int[][] result = new int[input[0].length][input.length];
        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < input.length; ++j) {
                result[i][j] = input[j][i];
            }
        }

        return result;
    }

    private static boolean valuesMatchRule(int[][] rule, int[] values) {
        // All values must match at least one of the rules
        return Arrays.stream(values).allMatch(v -> Arrays.stream(rule).anyMatch(range -> fieldInRange(range, v)));
    }

    private static Map<String, Integer> getFieldNames() {
        int[] errorRates = getErrorRates().toArray();
        int[][] validTickets = IntStream.range(0, errorRates.length).filter(i -> errorRates[i] == -1)
                                        .mapToObj(i -> nearbyTickets[i]).toArray(int[][]::new);

        int[][] fields = transposeArray(validTickets);

        List<List<String>> mappings = new ArrayList<>();
        for (int i = 0; i < fields.length; ++i) {
            int fin = i;
            mappings.add(fieldRules.entrySet().stream()
                                   .filter(rule -> valuesMatchRule(rule.getValue(), fields[fin]))
                                   .map(Map.Entry::getKey).collect(Collectors.toList()));
            // If exactly one rule matches the field (unique mapping), don't try to match it against other fields
            if (mappings.get(i).size() == 1) { fieldRules.remove(mappings.get(i).get(0)); }
        }
        // Order `mappings` so that the count of matches for each field is rising
        Integer[] indices = IntStream.range(0, mappings.size()).boxed()
                                     .sorted(Comparator.comparing(i -> mappings.get(i).size())).toArray(Integer[]::new);
        // Make unique mappings (if a mapping is unique for some field, it is removed for other fields)
        for (int i = 0; i < indices.length; ++i) {
            if (mappings.get(indices[i]).size() > 1) {
                throw new AssertionError("size of mapping is not 1");
            }

            String uniqueMapping = mappings.get(indices[i]).get(0);
            for (int j = i + 1; j < indices.length; ++j) {
                mappings.get(indices[j]).remove(uniqueMapping);
            }
        }

        return IntStream.range(0, mappings.size()).boxed()
                        .collect(Collectors.toMap(i -> mappings.get(i).get(0), i -> i));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            readInput(filename);

            System.out.println("Part 1: " + getErrorRates()
                                                .filter(rate -> rate != -1).sum());
            System.out.println("Part 2: " + getFieldNames().entrySet().stream()
                                                .filter(e -> e.getKey().startsWith("departure "))
                                                .mapToLong(e -> myTicket[e.getValue()]).reduce(1, (a, b) -> a * b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
