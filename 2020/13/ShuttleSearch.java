import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShuttleSearch {
    private static List<String> readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.collect(Collectors.toList());
        }
    }

    public static int timeToArrival(int timestamp, int busID) {
        return busID - timestamp % busID;
    }

    public static int part1(List<String> lines) {
        int timestamp = Integer.parseInt(lines.get(0));
        int earliestBusID = Arrays.stream(lines.get(1).split(",")).filter(s -> !s.equals("x"))
                                  .map(Integer::parseInt)
                                  .min(Comparator.comparingInt(busID -> timeToArrival(timestamp, busID))).get();

        return earliestBusID * timeToArrival(timestamp, earliestBusID);
    }

    public static long part2(List<String> lines) {
        String[] shuttles = lines.get(1).split(",");
        int[] shuttleNumbers = new int[shuttles.length];
        int[] shuttleOffsets = new int[shuttles.length];

        int shuttleCount = 0;
        for (int i = 0; i < shuttles.length; ++i) {
            if (!shuttles[i].equals("x")) {
                shuttleNumbers[shuttleCount] = Integer.parseInt(shuttles[i]);
                shuttleOffsets[shuttleCount] = i;
                ++shuttleCount;
            }
        }

        long timestamp = 0, stride = shuttleNumbers[0];
        for (int i = 1; i < shuttleCount;) {
            timestamp += stride;

            if ((timestamp + shuttleOffsets[i]) % shuttleNumbers[i] == 0) {
                stride *= shuttleNumbers[i++];
            }
        }

        return timestamp;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            List<String> lines = readLines(filename);

            System.out.println("Part 1: " + part1(lines));
            System.out.println("Part 2: " + part2(lines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
