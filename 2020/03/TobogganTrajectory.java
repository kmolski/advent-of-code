import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

enum GridSquare {
    Open,
    Tree
}

public class TobogganTrajectory {
    private static GridSquare[][] readLines(String filename) throws IOException {
        return Files.lines(Paths.get(filename))
                    .map(line -> line.chars().mapToObj(c -> (c == '#') ? GridSquare.Tree : GridSquare.Open)
                                             .toArray(GridSquare[]::new))
                    .toArray(GridSquare[][]::new);
    }

    private static long part1(GridSquare[][] grid) {
        long treeCount = 0;

        for (int i = 0, j = 0; i < grid.length; i += 1, j += 3) {
            if (grid[i][j % grid[i].length] == GridSquare.Tree) {
                treeCount += 1;
            }
        }

        return treeCount;
    }

    private static long part2(GridSquare[][] grid) {
        // for desc in slopeDescriptors, desc[0] is the number of squares to the right
        // desc[1] is the number of squares down
        int[][] slopeDescriptors = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
        long[] treeCounts = {0, 0, 0, 0, 0};

        for (int n = 0; n < 5; ++n) {
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

            System.out.println("Part 1: " + part1(grid));
            System.out.println("Part 2: " + part2(grid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
