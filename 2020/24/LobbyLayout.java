import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LobbyLayout {
    private static List<Integer> pathToCoordinate(String path) {
        int x = 0, y = 0, z = 0;

        for (int i = 0; i < path.length(); ++i) {
            switch (path.substring(i, Math.min(path.length(), i + 2))) {
                case "w":  case "ww":
                case "wn": case "ws":
                case "we":
                    x -= 1; y += 1;
                    break;
                case "nw":
                    y += 1; z -= 1;
                    ++i;
                    break;
                case "sw":
                    x -= 1; z += 1;
                    ++i;
                    break;
                case "e":  case "ee":
                case "en": case "es":
                case "ew":
                    x += 1; y -= 1;
                    break;
                case "ne":
                    x += 1; z -= 1;
                    ++i;
                    break;
                case "se":
                    y -= 1; z += 1;
                    ++i;
                    break;
            }
        }

        return List.of(x, y, z);
    }

    private static List<List<Integer>> readCoordinates(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(LobbyLayout::pathToCoordinate).collect(Collectors.toList());
        }
    }

    private static Set<List<Integer>> part1(List<List<Integer>> coordinates) {
        Set<List<Integer>> flips = new HashSet<>();

        for (List<Integer> coordinate : coordinates) {
            if (flips.contains(coordinate)) {
                flips.remove(coordinate);
            } else {
                flips.add(coordinate);
            }
        }

        return flips;
    }

    private static Stream<List<Integer>> getAdjacentTiles(List<Integer> coordinate) {
        int x = coordinate.get(0), y = coordinate.get(1), z = coordinate.get(2);
        return Stream.of(List.of(x - 1, y + 1, z), List.of(x, y + 1, z - 1), List.of(x + 1, y, z - 1),
                         List.of(x + 1, y - 1, z), List.of(x, y - 1, z + 1), List.of(x - 1, y, z + 1));
    }

    private static Set<List<Integer>> generateCoordinates(int maxDimension) {
        return IntStream.rangeClosed(-maxDimension, +maxDimension).boxed().flatMap(x ->
                   IntStream.rangeClosed(Math.max(-maxDimension, -x - maxDimension),
                                         Math.min(+maxDimension, -x + maxDimension))
                            .mapToObj(y -> List.of(x, y, -x - y)))
                   .collect(Collectors.toSet());
    }

    private static int part2(Set<List<Integer>> flips, int iterations) {
        int maxCoordinate = flips.stream().flatMapToInt(c -> c.stream().mapToInt(Integer::intValue)).max().getAsInt();

        for (int i = 1; i <= iterations; ++i) {
            Set<List<Integer>> copy = new HashSet<>(flips);

            for (List<Integer> coordinate : generateCoordinates(maxCoordinate + i)) {
                long adjacentCount = getAdjacentTiles(coordinate).filter(flips::contains).count();
                if (flips.contains(coordinate)) { // black tile
                    if (adjacentCount == 0 || adjacentCount > 2) {
                        copy.remove(coordinate);
                    }
                } else { // white tile
                    if (adjacentCount == 2) {
                        copy.add(coordinate);
                    }
                }
            }

            flips = copy;
        }

        return flips.size();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<List<Integer>> coordinates = readCoordinates(filename);
            Set<List<Integer>> flippedTiles = part1(coordinates);

            System.out.println("Part 1: " + flippedTiles.size());
            System.out.println("Part 2: " + part2(flippedTiles, 100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
