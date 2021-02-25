import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

class Haversack {
    private final String name;
    private final Set<String> containedBags;

    public Haversack(String line) {
        String[] details = line.split(" contain ");
        name = details[0].replaceAll(" bags", "");

        if (details[1].equals("no other bags.")) {
            containedBags = Set.of();
        } else {
            containedBags = Arrays.stream(details[1].split(", "))
                                  .map(desc -> desc.replaceAll(" bag.*", ""))
                                  .collect(Collectors.toSet());
        }
    }

    public Set<Haversack> getContainers(Map<String, Haversack> others) {
        Set<Haversack> allContainers = new HashSet<>();
        Set<Haversack> newContainers = Set.of(this);

        while (!newContainers.isEmpty()) {
            Set<String> keys = newContainers.stream().map(e -> e.name).collect(Collectors.toSet());
            newContainers = others.values().stream()
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

    public int getNumberOfBags(Map<String, Haversack> others) {
        return 1 + containedBags.stream().map(d -> d.split(" ", 2))
                                         .map(s -> Integer.parseInt(s[0]) * others.get(s[1]).getNumberOfBags(others))
                                         .reduce(0, Integer::sum);
    }

    public String getName() {
        return name;
    }
}

public class HandyHaversacks {
    private static Map<String, Haversack> readEntries(String filename) throws IOException {
        return Files.lines(Paths.get(filename)).map(Haversack::new)
                                               .collect(Collectors.toMap(Haversack::getName, h -> h));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            Map<String, Haversack> haversacks = readEntries(filename);

            System.out.println("Part 1: " + haversacks.get("shiny gold").getContainers(haversacks).size());
            // Excluding the shiny gold bag itself.
            System.out.println("Part 2: " + (haversacks.get("shiny gold").getNumberOfBags(haversacks) - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
