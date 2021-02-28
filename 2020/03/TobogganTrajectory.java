import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

enum GridSquare {
    Open,
    Tree
}

public class TobogganTrajectory {
    private static GridSquare[][] readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(line -> line.chars().mapToObj(c -> (c == '#') ? GridSquare.Tree : GridSquare.Open)
                                                 .toArray(GridSquare[]::new))
                        .toArray(GridSquare[][]::new);
        }
    }

    private static long solve(GridSquare[][] grid, int[][] slopeDescriptors) {
        // for desc in slopeDescriptors, desc[0] is the number of squares to the right
        // desc[1] is the number of squares down
        long[] treeCounts = new long[slopeDescriptors.length];

        for (int n = 0; n < slopeDescriptors.length; ++n) {
            for (int i = 0, j = 0; i < grid.length; i += slopeDescriptors[n][1], j += slopeDescriptors[n][0]) {
                if (grid[i][j % grid[i].length] == GridSquare.Tree) {
                    treeCounts[n] += 1;
                }
            }
        }

        return Arrays.stream(treeCounts).reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            GridSquare[][] grid = readLines(filename);

            System.out.println("Part 1: " + solve(grid, new int[][] {{3, 1}}));
            System.out.println("Part 2: " + solve(grid, new int[][] {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}}));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
