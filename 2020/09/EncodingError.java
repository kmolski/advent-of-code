import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EncodingError {
    private static List<Long> readInts(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(Long::parseLong).collect(Collectors.toList());
        }
    }

    private static boolean containsAddendsForSum(Collection<Long> collection, long sum) {
        return collection.stream().anyMatch(i -> (sum - i != i) && collection.contains(sum - i));
    }

    private static long part1(List<Long> ints, int preambleLength) {
        Deque<Long> deque = new ArrayDeque<>(preambleLength);
        for (int i = 0; i < preambleLength; ++i) {
            deque.addLast(ints.get(i));
        }

        for (int i = preambleLength; i < ints.size(); ++i) {
            long current = ints.get(i);
            if (!containsAddendsForSum(deque, current)) return current;

            deque.removeFirst();
            deque.addLast(current);
        }

        throw new AssertionError("No encoding error found!");
    }

    private static long part2(List<Long> ints, long sum) {
        Deque<Long> deque = new ArrayDeque<>();

        int sequenceStart = 0;
        for (int i = 0; i < ints.size(); ++i) {
            long current = ints.get(i);
            deque.addLast(current);

            long currentSum = deque.stream().mapToLong(Long::longValue).sum();
            while (currentSum > sum) { // Drop ints from the sequence while `currentSum` is over the target
                currentSum -= ints.get(sequenceStart++);
                deque.removeFirst();
            }

            if (i - sequenceStart >= 1 && currentSum == sum) {
                LongSummaryStatistics stats = deque.stream().mapToLong(Long::longValue).summaryStatistics();
                return stats.getMax() + stats.getMin();
            }
        }

        throw new AssertionError("No weakness found!");
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Long> ints = readInts(filename);

            long firstInvalid = part1(ints, 25);
            System.out.println("Part 1: " + firstInvalid);
            System.out.println("Part 2: " + part2(ints, firstInvalid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
