import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportRepair {
    private static Set<Integer> readInts(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).map(Integer::parseInt).collect(Collectors.toSet());
    }

    private static Set<Integer> solve(Set<Integer> entries, int target, int entryCount) {
        if (entryCount == 1) {
            Set<Integer> set = new HashSet<>();
            if (entries.contains(target)) { set.add(target); }
            return set;
        }

        return entries.stream().map(i -> {
            Set<Integer> set = solve(entries, target - i, entryCount - 1);
            set.add(i);
            return set;
        }).filter(set -> set.stream().reduce(0, Integer::sum) == target && set.size() == entryCount)
          .findFirst().orElseGet(HashSet::new);
    }

    private static int product(Set<Integer> set) {
        return set.stream().reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            Set<Integer> entries = readInts(filename);
            System.out.println("Part 1: " + product(solve(entries, 2020, 2)));
            System.out.println("Part 2: " + product(solve(entries, 2020, 3)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
