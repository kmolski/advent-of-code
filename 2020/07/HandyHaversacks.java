import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class Haversack {
    public static Map<String, Haversack> allHaversacks = new HashMap<>();

    private final String name;
    private final Set<String> containedBags;

    public Haversack(String line) {
        String[] details = line.split(" contain ");

        name = details[0].replaceAll(" bags", "");
        allHaversacks.put(name, this);

        if (details[1].equals("no other bags.")) {
            containedBags = Set.of();
        } else {
            containedBags = Arrays.stream(details[1].split(", "))
                                  .map(desc -> desc.replaceAll(" bag.*", ""))
                                  .collect(Collectors.toSet());
        }
    }

    public Set<Haversack> getContainers() {
        Set<Haversack> allContainers = new HashSet<>();
        Set<Haversack> newContainers = Set.of(this);

        while (!newContainers.isEmpty()) {
            Set<String> keys = newContainers.stream().map(e -> e.name).collect(Collectors.toSet());
            newContainers = allHaversacks.values().stream()
                    .filter(e -> {
                        Set<String> copy = e.containedBags.stream().map(c -> c.split(" ", 2)[1])
                                                                   .collect(Collectors.toSet());
                        copy.retainAll(keys);
                        return !copy.isEmpty();
                    })
                    .collect(Collectors.toSet());

            allContainers.addAll(newContainers);
        }

        return allContainers;
    }

    public int getNumberOfBags() {
        return 1 + containedBags.stream().map(d -> d.split(" ", 2))
                                         .map(s -> Integer.parseInt(s[0]) * allHaversacks.get(s[1]).getNumberOfBags())
                                         .reduce(0, Integer::sum);
    }
}

public class HandyHaversacks {
    private static void readEntries(String filename) throws IOException {
        Files.lines(Paths.get(filename)).forEach(Haversack::new);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            readEntries(filename);

            System.out.println("Part 1: " + Haversack.allHaversacks.get("shiny gold").getContainers().size());
            // Excluding the shiny gold bag itself.
            System.out.println("Part 2: " + (Haversack.allHaversacks.get("shiny gold").getNumberOfBags() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
