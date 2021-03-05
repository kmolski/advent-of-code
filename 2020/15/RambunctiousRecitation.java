import java.util.Arrays;

public class RambunctiousRecitation {
    public static int part1(int[] initialNumbers, int limit) {
        int[] prevAppearanceCounts = new int[limit];
        int[] lastAppearanceIndices = new int[limit];

        int i = 0, lastNumber = 0;
        for (; i < initialNumbers.length; ++i) {
            lastNumber = initialNumbers[i];
            prevAppearanceCounts[lastNumber] = 1;
            lastAppearanceIndices[lastNumber] = i + 1;
        }

        for (; i < limit; ++i) {
            int prevNumber = lastNumber;
            lastNumber = (prevAppearanceCounts[prevNumber] == 1) ? 0 : i - lastAppearanceIndices[prevNumber];
            lastAppearanceIndices[prevNumber] = i;

            if (prevAppearanceCounts[lastNumber] == 0) {
                prevAppearanceCounts[lastNumber] = 1;
                lastAppearanceIndices[lastNumber] = i + 1;
            } else {
                prevAppearanceCounts[lastNumber] += 1;
            }
        }

        return lastNumber;
    }

    public static void main(String[] args) {
        String numbers = "5,1,9,18,13,8,0";
        int[] initialNumbers = Arrays.stream(numbers.split(",")).mapToInt(Integer::parseInt).toArray();

        System.out.println("Part 1: " + part1(initialNumbers, 2020));
        System.out.println("Part 2: " + part1(initialNumbers, 30000000));
    }
}
