import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class StringUtils {
    public static String[] transpose(String[] input) {
        return IntStream.range(0, input[0].length()).mapToObj(j ->
                   Arrays.stream(input).mapToInt(s -> s.charAt(j))
                                       .collect(StringBuffer::new, StringBuffer::appendCodePoint, StringBuffer::append)
                                       .toString())
                        .toArray(String[]::new);
    }

    public static String[] flipVertically(String[] input) {
        return IntStream.range(0, input.length).mapToObj(i -> input[input.length - 1 - i]).toArray(String[]::new);
    }

    public static String[] flipHorizontally(String[] input) {
        return Arrays.stream(input).map(l -> new StringBuilder(l).reverse().toString()).toArray(String[]::new);
    }
}

class ImageTile {
    public static final int UP = 0, LEFT = 1, DOWN = 2, RIGHT = 3;

    private static final Map<String, List<Long>> edgeToTileIDs = new HashMap<>();

    private final long ID;
    private String[] data;
    private final List<String> edges;

    public static void countEdges(List<ImageTile> tiles) {
        for (ImageTile tile : tiles) {
            for (String edge : tile.edges) {
                if (edgeToTileIDs.containsKey(edge)) {
                    edgeToTileIDs.get(edge).add(tile.getID());
                } else {
                    edgeToTileIDs.put(edge, new ArrayList<>(List.of(tile.getID())));
                }
            }
        }
    }

    private static String normalizeEdge(String edge) {
        return Collections.min(List.of(edge, new StringBuilder(edge).reverse().toString()));
    }

    public ImageTile(String entry) {
        ID = entry.lines().findFirst().map(l -> Long.parseLong(l.replaceAll("\\D", ""))).get();
        data = entry.lines().skip(1).toArray(String[]::new);

        String[] transposed = StringUtils.transpose(data);
        edges = Stream.of(data[0], transposed[0], data[data.length - 1], transposed[transposed.length - 1])
                      .map(ImageTile::normalizeEdge).collect(Collectors.toList());
    }

    public long getID() { return ID; }

    public String[] getTruncatedData() {
        return Arrays.stream(data).skip(1).map(line -> line.substring(1, line.length() - 1))
                                          .limit(data.length - 2).toArray(String[]::new);
    }

    public Stream<String> getUniqueEdges() { return edges.stream().filter(e -> edgeToTileIDs.get(e).size() == 1); }

    public String getCommonEdge(ImageTile other) {
        List<String> copy = new ArrayList<>(edges);
        copy.retainAll(other.edges);
        return copy.size() > 0 ? copy.get(0) : null;
    }

    public boolean isEdge() { return getUniqueEdges().count() == 1; }
    public boolean isCorner() { return getUniqueEdges().count() == 2; }
    public boolean isEdgeOrCorner() { return isEdge() || isCorner(); }
    public boolean bordersWith(ImageTile other) { return getCommonEdge(other) != null; }

    public void flipVertically() {
        data = StringUtils.flipVertically(data);
        Collections.swap(edges, UP, DOWN);
    }

    public void flipHorizontally() {
        data = StringUtils.flipHorizontally(data);
        Collections.swap(edges, LEFT, RIGHT);
    }

    public void transpose() {
        data = StringUtils.transpose(data);
        Collections.swap(edges, UP, LEFT); Collections.swap(edges, DOWN, RIGHT);
    }

    public void pointTo(ImageTile left, ImageTile up) {
        switch (edges.indexOf((left != null) ? getCommonEdge(left) : getUniqueEdges().findFirst().get())) {
            case DOWN:
                flipVertically();
            case UP:
                transpose();
                break;
            case RIGHT:
                flipHorizontally();
        }
        switch (edges.indexOf((up != null) ? getCommonEdge(up) : getUniqueEdges().findFirst().get())) {
            case RIGHT:
                flipHorizontally();
            case LEFT:
                transpose();
                break;
            case DOWN:
                flipVertically();
        }
    }
}

public class JurassicJigsaw {
    private static final String[] seaMonster = {"..................#.", "#....##....##....###", ".#..#..#..#..#..#..."};

    private static final Set<List<Integer>> seaMonsterIndicesTemplate =
        IntStream.range(0, seaMonster.length).boxed().flatMap(i ->
            IntStream.range(0, seaMonster[i].length()).filter(j -> seaMonster[i].charAt(j) == '#')
                                                      .mapToObj(j -> List.of(i, j)))
                                             .collect(Collectors.toSet());

    private static List<ImageTile> readTiles(String filename) throws IOException {
        String fileContents = Files.readString(Paths.get(filename));
        return Arrays.stream(fileContents.split("\\n{2,}")).map(ImageTile::new).collect(Collectors.toList());
    }

    private static ImageTile take(List<ImageTile> tiles, Function<Stream<ImageTile>, Stream<ImageTile>> filter) {
        ImageTile result = filter.apply(tiles.stream()).findFirst().get();
        tiles.remove(result);
        return result;
    }

    private static Stream<String> tileRowToLines(Stream<ImageTile> tileRow) {
        return Arrays.stream(tileRow.filter(Objects::nonNull).map(ImageTile::getTruncatedData)
                                    .reduce((acc, data) -> IntStream.range(0, acc.length)
                                                                    .mapToObj(i -> acc[i] + data[i])
                                                                    .toArray(String[]::new)).get());
    }

    private static String[] mergeImage(List<ImageTile> tiles) {
        ImageTile[][] orderedTiles = new ImageTile[tiles.size()][];
        // Pick a corner tile to start from
        orderedTiles[0] = new ImageTile[tiles.size()];
        orderedTiles[0][0] = take(tiles, s -> s.filter(ImageTile::isCorner));
        // Fill the first row with more edge/corner tiles that match
        int rowLength;
        for (rowLength = 1; rowLength < tiles.size(); ++rowLength) {
            ImageTile left = orderedTiles[0][rowLength - 1];
            // Edge/corner tiles in the first row have to match the tile to the left
            orderedTiles[0][rowLength] = take(tiles, s -> s.filter(ImageTile::isEdgeOrCorner).filter(left::bordersWith));
            orderedTiles[0][rowLength].pointTo(left, null);
            // Loop until a corner tile appears
            if (orderedTiles[0][rowLength].isCorner()) { ++rowLength; break; }
        }

        int rowCount = tiles.size() / rowLength + 1;
        for (int i = 1; i < rowCount; ++i) {
            orderedTiles[i] = new ImageTile[rowLength];
            ImageTile above = orderedTiles[i - 1][0]; // Find the edge/corner tile for this row
            orderedTiles[i][0] = take(tiles, s -> s.filter(ImageTile::isEdgeOrCorner).filter(above::bordersWith));
            orderedTiles[i][0].pointTo(null, above);

            for (int j = 1; j < rowLength; ++j) {
                ImageTile left = orderedTiles[i][j - 1], up = orderedTiles[i - 1][j];
                // Further tiles have to match both the tile to the left and the tile above
                orderedTiles[i][j] = take(tiles, s -> s.filter(left::bordersWith).filter(up::bordersWith));
                orderedTiles[i][j].pointTo(left, up);
            }
        }

        orderedTiles[0][0].pointTo(orderedTiles[0][1], orderedTiles[1][0]);
        orderedTiles[0][0].flipHorizontally();
        orderedTiles[0][0].flipVertically();

        return Arrays.stream(orderedTiles).filter(Objects::nonNull).map(Arrays::stream)
                                          .flatMap(JurassicJigsaw::tileRowToLines).toArray(String[]::new);
    }

    private static long part1(List<ImageTile> tiles) {
        return tiles.stream().filter(ImageTile::isCorner).map(ImageTile::getID).reduce(1L, (a, b) -> a * b);
    }

    private static boolean windowContainsSeaMonster(String[] image, int i, int j) {
        int k;
        for (k = 0; k < seaMonster.length; ++k) {
            if (!image[i + k].substring(j, j + seaMonster[0].length()).matches(seaMonster[k])) { break; }
        }
        return k == seaMonster.length;
    }

    private static long part2(List<ImageTile> tiles) {
        String[] image = mergeImage(tiles);
        Set<List<Integer>> seaMonsterIndices = new HashSet<>();

        for (int n = 0; n < 8; ++n) {
            String[] copy = image.clone();
            // Flip/transpose the image to produce unique orientations
            if ((n & 0b001) != 0) { copy = StringUtils.flipHorizontally(copy); }
            if ((n & 0b010) != 0) { copy = StringUtils.flipVertically(copy); }
            if ((n & 0b100) != 0) { copy = StringUtils.transpose(copy); }

            int rows = copy.length - seaMonster.length, columns = copy[0].length() - seaMonster[0].length();
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < columns; ++j) {
                    if (windowContainsSeaMonster(copy, i, j)) {
                        int matchI = i, matchJ = j;
                        seaMonsterIndices.addAll(seaMonsterIndicesTemplate.stream().map(index ->
                                List.of(index.get(0) + matchI, index.get(1) + matchJ)).collect(Collectors.toSet()));
                    }
                }
            }
        }
        // The match indices are stored in a set, so overlapping monster tiles will only be counted once
        return Arrays.stream(image).mapToLong(row -> row.chars().filter(c -> c == '#').count())
                                   .sum() - seaMonsterIndices.size();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<ImageTile> tiles = readTiles(filename);
            ImageTile.countEdges(tiles);

            System.out.println("Part 1: " + part1(tiles));
            System.out.println("Part 2: " + part2(tiles));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
