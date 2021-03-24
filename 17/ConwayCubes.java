import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Coordinates {
    private final int[] coordinates;
    public Coordinates(int... pos) { coordinates = pos; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (getClass() != obj.getClass()) { return false; }
        return Arrays.equals(((Coordinates) obj).coordinates, coordinates);
    }

    @Override
    public int hashCode() { return Arrays.hashCode(coordinates); }

    public int[] getArray() { return coordinates; }
    public int min() { return Arrays.stream(coordinates).min().orElse(0); }
    public int max() { return Arrays.stream(coordinates).max().orElse(0); }
}

public class ConwayCubes {
    private static Set<Coordinates> readFile(String filename) throws IOException {
        String[] lines = Files.readString(Paths.get(filename)).split("\n");

        return IntStream.range(0, lines.length).boxed().flatMap(y ->
                   IntStream.range(0, lines[y].length()).filter(x -> lines[y].charAt(x) == '#')
                                                        .mapToObj(x -> new Coordinates(x, y))
        ).collect(Collectors.toSet());
    }

    private static Stream<int[]> generateCoordinates(int dimensions, int i, int min, int max) {
        if (i == 0) { return Stream.of(new int[dimensions]); }

        return generateCoordinates(dimensions, i - 1, min, max)
                   .flatMap(pos -> IntStream.rangeClosed(min, max)
                       .mapToObj(n -> {
                           int[] extended = Arrays.copyOf(pos, dimensions);
                           extended[i - 1] = n; return extended;
                       }));
    }

    private static Stream<Coordinates> generateCoordinates(int dimensions, int min, int max) {
        return generateCoordinates(dimensions, dimensions, min, max).map(Coordinates::new);
    }

    private static Set<Coordinates> extendIndices(Set<Coordinates> src, int dimensions) {
        return src.stream().map(pos -> new Coordinates(Arrays.copyOf(pos.getArray(), dimensions)))
                           .collect(Collectors.toSet());
    }

    private static long countAdjacent(Set<Coordinates> state, Coordinates pos) {
        int dimensions = pos.getArray().length;
        return generateCoordinates(dimensions, dimensions, -1, 1)
                .map(offset -> new Coordinates(IntStream.range(0, dimensions).map(i -> pos.getArray()[i] + offset[i])
                                                                             .toArray()))
                .filter(adjacent -> !adjacent.equals(pos))
                .filter(state::contains).count();
    }

    private static long solve(Set<Coordinates> initialState, int count, int dimensions) {
        Set<Coordinates> state = extendIndices(initialState, dimensions);

        for (int n = 0; n < count; ++n) {
            Set<Coordinates> oldState = new HashSet<>(state);
            int minDimension = state.stream().mapToInt(Coordinates::min).min().getAsInt();
            int maxDimension = state.stream().mapToInt(Coordinates::max).max().getAsInt();

            Set<Coordinates> indices = generateCoordinates(dimensions, minDimension - 1, maxDimension + 1)
                                           .collect(Collectors.toSet());

            for (Coordinates pos : indices) {
                long adjacent = countAdjacent(oldState, pos);
                if ((oldState.contains(pos) && adjacent == 2) || adjacent == 3) {
                    state.add(pos);
                } else {
                    state.remove(pos);
                }
            }
        }

        return state.size();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            Set<Coordinates> init = readFile(filename);

            System.out.println("Part 1: " + solve(init, 6, 3));
            System.out.println("Part 2: " + solve(init, 6, 4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
