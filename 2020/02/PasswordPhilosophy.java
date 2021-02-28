import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PasswordPhilosophy {
    private static List<String> readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.collect(Collectors.toList());
        }
    }

    private static boolean part1(String line) {
        String[] spaceSplits = line.split(" ");
        String[] indexRanges = spaceSplits[0].split("-");
        char letter = spaceSplits[1].charAt(0);
        String password = spaceSplits[2];

        long lowBound = Long.parseLong(indexRanges[0]);
        long highBound = Long.parseLong(indexRanges[1]);
        long letterCount = password.chars().filter(c -> c == letter).count();

        return (lowBound <= letterCount) && (letterCount <= highBound);
    }

    private static boolean part2(String line) {
        String[] spaceSplits = line.split(" ");
        String[] indexRanges = spaceSplits[0].split("-");
        char letter = spaceSplits[1].charAt(0);
        String password = spaceSplits[2];

        return Arrays.stream(indexRanges).map(i -> Integer.parseInt(i) - 1)
                     .filter(i -> password.charAt(i) == letter).count() == 1;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<String> lines = readLines(filename);
            System.out.println("Part 1: " + lines.stream().filter(PasswordPhilosophy::part1).count());
            System.out.println("Part 2: " + lines.stream().filter(PasswordPhilosophy::part2).count());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
