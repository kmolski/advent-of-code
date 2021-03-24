import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrabCups {
    private static int next(int[] arr, int i, int n) {
        return (n == 1) ? arr[i] : next(arr, arr[i], n - 1);
    }

    public static int[] solve(List<Integer> cups, int moves) {
        int maxCup = cups.stream().mapToInt(Integer::intValue).max().getAsInt();
        int cupCount = cups.size();

        int[] cupsArr = new int[maxCup + 1];
        for (int i = 0; i < cups.size(); ++i) {
            cupsArr[cups.get(i)] = cups.get((i + 1) % cupCount);
        }

        int current = cups.get(0);
        for (int i = 0; i < moves; ++i) {
            cupsArr[0] = cupsArr[current]; // Save the position of the three cups clockwise to the current cup
            cupsArr[current] = next(cupsArr, current, 4); // Skip the three cups
            // Find the destination cup (has to be outside the picked up cups)
            int dest = Math.floorMod(current - 2, maxCup) + 1;
            while (next(cupsArr, 0, 1) == dest
                    || next(cupsArr, 0, 2) == dest
                    || next(cupsArr, 0, 3) == dest) {
                dest = Math.floorMod(dest - 2, maxCup) + 1;
            }

            int followingCup = next(cupsArr, dest, 1); // Save the position of the cup after the destination
            cupsArr[dest] = cupsArr[0]; // Insert the three picked up cups after the destination
            cupsArr[next(cupsArr, dest, 3)] = followingCup; // Point the 3rd picked up cup to the `following` cup

            current = next(cupsArr, current, 1);
        }

        return cupsArr;
    }

    private static String part1(List<Integer> cups, int moves) {
        int[] solution = solve(cups, moves);

        StringBuilder result = new StringBuilder();
        for (int i = 1; solution[i] != 1; i = next(solution, i, 1)) {
            result.append(solution[i]);
        }

        return result.toString();
    }

    private static long part2(List<Integer> cups, int moves) {
        List<Integer> extended = new ArrayList<>(cups);
        int maxCup = extended.stream().mapToInt(Integer::intValue).max().getAsInt();
        extended.addAll(IntStream.rangeClosed(maxCup + 1, 1_000_000).boxed().collect(Collectors.toList()));

        int[] solution = solve(extended, moves);
        return (long) next(solution, 1, 1) * next(solution, 1, 2);
    }

    public static void main(String[] args) {
        String numbers = "853192647";
        List<Integer> cups = numbers.chars().mapToObj(c -> c - '0').collect(Collectors.toList());

        System.out.println("Part 1: " + part1(cups, 100));
        System.out.println("Part 2: " + part2(cups, 10_000_000));
    }
}
