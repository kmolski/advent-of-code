import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface Rule {
    int getIndex();
    Stream<String> apply(String input, Map<Integer, Rule> rules);
}

class CharacterRule implements Rule {
    private final int index;
    private final char character;

    public CharacterRule(String line) {
        String[] desc = line.split(": ");
        index = Integer.parseInt(desc[0]);
        character = desc[1].replaceAll("\\W", "").charAt(0);
    }

    @Override
    public Stream<String> apply(String input, Map<Integer, Rule> rules) {
        return (!input.isEmpty() && input.charAt(0) == character) ? Stream.of(input.substring(1)) : Stream.empty();
    }

    @Override
    public int getIndex() {
        return index;
    }
}

class CompositeRule implements Rule {
    private final int index;
    private final int[][] alternatives;

    public CompositeRule(String line) {
        String[] desc = line.split(": ");
        index = Integer.parseInt(desc[0]);
        alternatives = Arrays.stream(desc[1].split("\\|"))
                                            .map(sequence -> Arrays.stream(sequence.trim().split(" "))
                                                                   .mapToInt(Integer::parseInt).toArray())
                             .toArray(int[][]::new);
    }

    @Override
    public Stream<String> apply(String input, Map<Integer, Rule> rules) {
        return Arrays.stream(alternatives).flatMap(sequence -> {
            Stream<String> remainders = Stream.of(input);
            for (int subRule : sequence) {
                remainders = remainders.flatMap(rem -> rules.get(subRule).apply(rem, rules));
            }
            return remainders;
        });
    }

    @Override
    public int getIndex() {
        return index;
    }
}

public class MonsterMessages {
    private static Map<Integer, Rule> rules;
    private static String[] messages;

    private static void readFile(String filename) throws IOException {
        String[] parts = Files.readString(Paths.get(filename)).split("\n{2,}");

        rules = parts[0].lines().map(line -> (line.matches("\\d+:\\s*\"\\w\""))
                                              ? new CharacterRule(line) : new CompositeRule(line))
                        .collect(Collectors.toMap(Rule::getIndex, r -> r));

        messages = parts[1].lines().toArray(String[]::new);
    }

    private static long solve() {
        return Arrays.stream(messages).filter(m -> rules.get(0).apply(m, rules).anyMatch(rem -> rem.equals("")))
                                      .count();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            readFile(filename);

            System.out.println("Part 1: " + solve());
            rules.put(8, new CompositeRule("8: 42 | 42 8"));
            rules.put(11, new CompositeRule("11: 42 31 | 42 11 31"));
            System.out.println("Part 2: " + solve());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
