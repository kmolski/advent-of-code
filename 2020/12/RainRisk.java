import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Ferry {
    private final String[] instructions;
    private int longitude = 0;
    private int latitude = 0;
    private int waypointLongitude = 10;
    private int waypointLatitude = 1;
    private int heading = 90;

    public Ferry(String fileContents) {
        instructions = fileContents.lines().toArray(String[]::new);
    }

    public void reset() {
        longitude = latitude = 0;
        heading = 90;
        waypointLongitude = 10;
        waypointLatitude = 1;
    }

    public void part1() {
        for (String instruction : instructions) {
            int argument = Integer.parseInt(instruction.substring(1));

            switch (instruction.charAt(0)) {
                case 'N':
                    latitude += argument;
                    break;
                case 'E':
                    longitude += argument;
                    break;
                case 'S':
                    latitude -= argument;
                    break;
                case 'W':
                    longitude -= argument;
                    break;
                case 'L':
                    heading = Math.floorMod(heading - argument, 360);
                    break;
                case 'R':
                    heading = Math.floorMod(heading + argument, 360);
                    break;
                case 'F':
                    latitude += argument * Math.cos(heading / 180.0 * Math.PI);
                    longitude += argument * Math.sin(heading / 180.0 * Math.PI);
                    break;
            }
        }
    }

    public void part2() {
        for (String instruction : instructions) {
            int argument = Integer.parseInt(instruction.substring(1));
            double radianArg = argument / 180.0 * Math.PI;
            int prevLat = waypointLatitude;
            int prevLong = waypointLongitude;

            switch (instruction.charAt(0)) {
                case 'N':
                    waypointLatitude += argument;
                    break;
                case 'E':
                    waypointLongitude += argument;
                    break;
                case 'S':
                    waypointLatitude -= argument;
                    break;
                case 'W':
                    waypointLongitude -= argument;
                    break;
                case 'L':
                    waypointLatitude  = (int) Math.round(prevLat * Math.cos(radianArg)
                                                         + prevLong * Math.sin(radianArg));
                    waypointLongitude = (int) Math.round(prevLong * Math.cos(radianArg)
                                                         - prevLat * Math.sin(radianArg));
                    break;
                case 'R':
                    waypointLatitude  = (int) Math.round(prevLat * Math.cos(radianArg)
                                                         - prevLong * Math.sin(radianArg));
                    waypointLongitude = (int) Math.round(prevLong * Math.cos(radianArg)
                                                         + prevLat * Math.sin(radianArg));
                    break;
                case 'F':
                    latitude += argument * waypointLatitude;
                    longitude += argument * waypointLongitude;
                    break;
            }
        }
    }

    public int getManhattanDistance() {
        return Math.abs(latitude) + Math.abs(longitude);
    }
}

public class RainRisk {
    private static String readFile(String filename) throws IOException {
        return Files.readString(Paths.get(filename));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            String fileContents = readFile(filename);
            Ferry ferry = new Ferry(fileContents);

            ferry.part1();
            System.out.println("Part 1: " + ferry.getManhattanDistance());
            ferry.reset();
            ferry.part2();
            System.out.println("Part 2: " + ferry.getManhattanDistance());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
