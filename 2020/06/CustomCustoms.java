import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomCustoms {
    private static List<Set<String>> readGroups(String filename) throws IOException {
        String[] groups = Files.readString(Paths.get(filename)).split("\\n{2,}");
        return Arrays.stream(groups).map(group -> Arrays.stream(group.split("\n")).collect(Collectors.toSet()))
                                    .collect(Collectors.toList());
    }

    private static Stream<Character> stringToCharStream(String s) {
        return s.chars().mapToObj(c -> (char) c);
    }

    private static Set<Character> stringToCharBag(String s) {
        return stringToCharStream(s).collect(Collectors.toCollection(HashSet::new));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<Set<String>> groups = readGroups(filename);

            System.out.println("Part 1: " +
                    groups.stream().map(set -> set.stream().flatMap(CustomCustoms::stringToCharStream)
                                                           .distinct().count())
                                   .reduce(0L, Long::sum));

            System.out.println("Part 2: " +
                    groups.stream().map(set -> set.stream().map(CustomCustoms::stringToCharBag)
                                                           .reduce((a, b) -> { a.retainAll(b); return a; })
                                                           .map(Set::size).orElse(0))
                                   .reduce(0, Integer::sum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
