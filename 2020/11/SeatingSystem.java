import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

enum FerrySquare {
    Floor,
    Free,
    Occupied
}

public class SeatingSystem {
    private static FerrySquare[][] readGrid(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.map(line -> line.chars().mapToObj(c -> {
                                             switch (c) {
                                                 case 'L':
                                                     return FerrySquare.Free;
                                                 case '.':
                                                     return FerrySquare.Floor;
                                             }
                                             return null; })
                                         .toArray(FerrySquare[]::new))
                        .toArray(FerrySquare[][]::new);
        }
    }

    private static int countAdjacentOccupiedSeats(FerrySquare[][] grid, int x, int y) {
        int total = 0;

        for (int i = x - 1; i <= x + 1; ++i) {
            for (int j = y - 1; j <= y + 1; ++j) {
                if ((i != x || j != y) && 0 <= i && i < grid.length && 0 <= j && j < grid[i].length
                        && grid[i][j] == FerrySquare.Occupied) {
                    ++total;
                }
            }
        }

        return total;
    }

    private static int walkDirection(FerrySquare[][] grid, int x, int y, int stepX, int stepY) {
        for (int i = x + stepX, j = y + stepY;
                 i >= 0 && i < grid.length && j >= 0 && j < grid[i].length;
                 i += stepX, j += stepY) {
            if (grid[i][j] == FerrySquare.Occupied)  { return 1; }
            else if (grid[i][j] == FerrySquare.Free) { return 0; }
        }
        return 0;
    }

    private static int countVisibleOccupiedSeats(FerrySquare[][] grid, int x, int y) {
        return walkDirection(grid, x, y, -1, +0)
             + walkDirection(grid, x, y, +0, -1)
             + walkDirection(grid, x, y, +1, +0)
             + walkDirection(grid, x, y, +0, +1)
             + walkDirection(grid, x, y, +1, +1)
             + walkDirection(grid, x, y, -1, +1)
             + walkDirection(grid, x, y, +1, -1)
             + walkDirection(grid, x, y, -1, -1);
    }

    private static int iterate(FerrySquare[][] grid, boolean useVisibleSeats, int maxOccupied) {
        FerrySquare[][] oldGrid = Arrays.stream(grid).map(FerrySquare[]::clone).toArray(FerrySquare[][]::new);
        int changeCount = 0;

        for (int i = 0; i < oldGrid.length; ++i) {
            for (int j = 0; j < oldGrid[i].length; ++j) {
                int occupiedSeatsAround = useVisibleSeats ? countVisibleOccupiedSeats(oldGrid, i, j)
                                                          : countAdjacentOccupiedSeats(oldGrid, i, j);

                if (oldGrid[i][j] == FerrySquare.Free && occupiedSeatsAround == 0) {
                    ++changeCount;
                    grid[i][j] = FerrySquare.Occupied;
                } else if (oldGrid[i][j] == FerrySquare.Occupied && occupiedSeatsAround >= maxOccupied) {
                    ++changeCount;
                    grid[i][j] = FerrySquare.Free;
                }
            }
        }

        return changeCount;
    }

    private static long part1(FerrySquare[][] grid) {
        FerrySquare[][] copy = Arrays.stream(grid).map(FerrySquare[]::clone).toArray(FerrySquare[][]::new);

        while (iterate(copy, false, 4) > 0);

        return Arrays.stream(copy).mapToLong(row -> Arrays.stream(row).filter(s -> s == FerrySquare.Occupied)
                                                                      .count())
                                  .sum();
    }

    private static long part2(FerrySquare[][] grid) {
        FerrySquare[][] copy = Arrays.stream(grid).map(FerrySquare[]::clone).toArray(FerrySquare[][]::new);

        while (iterate(copy, true, 5) > 0);

        return Arrays.stream(copy).mapToLong(row -> Arrays.stream(row).filter(s -> s == FerrySquare.Occupied)
                                                                      .count())
                                  .sum();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            FerrySquare[][] grid = readGrid(filename);

            System.out.println("Part 1: " + part1(grid));
            System.out.println("Part 2: " + part2(grid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
