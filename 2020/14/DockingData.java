import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class DockingProgram {
    private final String[] statements;

    private String currentMask;
    private Map<Long, Long> memory;

    public DockingProgram(String[] lines) {
        statements = lines;
        reset();
    }

    public void reset() {
        currentMask = "";
        memory = new HashMap<>();
    }

    public static long[] stringToBitmasks(String bitmask, char setMark, char clearMark) {
        long andMask = -1L, orMask = 0L;

        for (int i = 0; i < bitmask.length(); ++i) {
            if (bitmask.charAt(i) == setMark) {
                orMask |= Long.rotateLeft(1L, 35 - i);
            } else if (bitmask.charAt(i) == clearMark) {
                andMask &= Long.rotateLeft(-2L, 35 - i);
            }
        }

        return new long[] {andMask, orMask};
    }

    public static long applyBitmasks(long value, long[] masks) {
        return (value & masks[0]) | masks[1];
    }

    public long part1() {
        for (String statement : statements) {
            String[] parts = statement.split(" = ", 2);

            if (parts[0].matches("mem\\[\\d+]")) {
                long address = Long.parseLong(parts[0].replaceAll("\\D", ""));

                long[] masks = stringToBitmasks(currentMask, '1', '0');
                memory.put(address, applyBitmasks(Long.parseLong(parts[1]), masks));
            } else if (parts[0].equals("mask")) {
                currentMask = parts[1];
            }
        }

        return memory.values().stream().reduce(0L, Long::sum);
    }

    public long part2() {
        int floatingBitCount = 0, combinationCount = 0;

        for (String statement : statements) {
            String[] parts = statement.split(" = ", 2);

            if (parts[0].matches("mem\\[\\d+]")) {
                long address = Long.parseLong(parts[0].replaceAll("\\D", ""));

                long[] setMask = stringToBitmasks(currentMask, '1', 'N');
                address = applyBitmasks(address, setMask);

                for (int i = 0; i < combinationCount; ++i) {
                    for (int j = 0, index = -1; j < floatingBitCount; ++j) {
                        index = currentMask.indexOf('X', index + 1);

                        if ((i & (1 << j)) == 0) {
                            address &= Long.rotateLeft(-2L, 35 - index);
                        } else {
                            address |= Long.rotateLeft(1L, 35 - index);
                        }
                    }

                    memory.put(address, Long.parseLong(parts[1]));
                }
            } else if (parts[0].equals("mask")) {
                currentMask = parts[1];
                floatingBitCount = (int) currentMask.chars().filter(c -> c == 'X').count();
                combinationCount = (int) Math.pow(2, floatingBitCount);
            }
        }

        return memory.values().stream().reduce(0L, Long::sum);
    }
}

public class DockingData {
    private static String[] readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.toArray(String[]::new);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            String[] lines = readLines(filename);
            DockingProgram program = new DockingProgram(lines);

            System.out.println("Part 1: " + program.part1());
            program.reset();
            System.out.println("Part 2: " + program.part2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
