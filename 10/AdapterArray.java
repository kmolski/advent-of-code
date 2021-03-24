import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdapterArray {
    private static List<Integer> readInts(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(Integer::parseInt).collect(Collectors.toList());
        }
    }

    private static int part1(List<Integer> ints) {
        int prevRating = 0, joltDiffsOf1 = 0, joltDiffsOf3 = 0;
        for (Integer rating : ints) {
            int diff = rating - prevRating;
            prevRating = rating;

            if (diff == 1) ++joltDiffsOf1;
            else if (diff == 3) ++joltDiffsOf3;
        }

        return joltDiffsOf1 * joltDiffsOf3;
    }

    private static long part2(List<Integer> ints) {
        long[] counts = new long[ints.size()];

        counts[0] = 1;
        for (int i = 1; i < ints.size(); ++i) {
            for (int j = i - 1; j >= 0 && ints.get(i) - ints.get(j) <= 3; --j) {
                counts[i] += counts[j];
            }
        }

        return counts[ints.size() - 1];
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Integer> ints = readInts(filename);

            Collections.sort(ints);
            int lastRating = ints.get(ints.size() - 1) + 3;
            ints.add(0, 0);
            ints.add(lastRating); // built-in adapter

            System.out.println("Part 1: " + part1(ints));
            System.out.println("Part 2: " + part2(ints));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
