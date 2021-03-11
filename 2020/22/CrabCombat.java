import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum Player { ONE, TWO }

public class CrabCombat {
    private static List<Integer> player1Cards, player2Cards;

    private static void readInput(String filename) throws IOException {
        String[] fileContents = Files.readString(Paths.get(filename)).split("\\n{2,}");

        player1Cards = fileContents[0].lines().skip(1).map(Integer::parseInt).collect(Collectors.toList());
        player2Cards = fileContents[1].lines().skip(1).map(Integer::parseInt).collect(Collectors.toList());
    }

    private static int calculateWinnersScore(Deque<Integer> p1Cards, Deque<Integer> p2Cards) {
        Deque<Integer> winnersDeck = p1Cards.isEmpty() ? p2Cards : p1Cards;
        return IntStream.iterate(1, i -> !winnersDeck.isEmpty(), i -> i + 1)
                        .map(i -> i * winnersDeck.removeLast()).sum();
    }

    private static int part1() {
        Deque<Integer> p1Cards = new ArrayDeque<>(player1Cards);
        Deque<Integer> p2Cards = new ArrayDeque<>(player2Cards);

        while (!p1Cards.isEmpty() && !p2Cards.isEmpty()) {
            if (p1Cards.peek() > p2Cards.peek()) {
                p1Cards.addLast(p1Cards.pop());
                p1Cards.addLast(p2Cards.pop());
            } else {
                p2Cards.addLast(p2Cards.pop());
                p2Cards.addLast(p1Cards.pop());
            }
        }
        return calculateWinnersScore(p1Cards, p2Cards);
    }

    private static Player playRecursive(Deque<Integer> p1Cards, Deque<Integer> p2Cards) {
        Set<List<List<Integer>>> deckConfigurations = new HashSet<>();

        while (!p1Cards.isEmpty() && !p2Cards.isEmpty()) {
            List<List<Integer>> currentDeckConfig = List.of(List.copyOf(p1Cards), List.copyOf(p2Cards));
            if (deckConfigurations.contains(currentDeckConfig)) { return Player.ONE; }

            Player winner;
            int p1Card = p1Cards.pop(), p2Card = p2Cards.pop();

            if (p1Cards.size() >= p1Card && p2Cards.size() >= p2Card) {
                winner = playRecursive(p1Cards.stream().limit(p1Card).collect(Collectors.toCollection(ArrayDeque::new)),
                                       p2Cards.stream().limit(p2Card).collect(Collectors.toCollection(ArrayDeque::new)));
            } else {
                winner = p1Card > p2Card ? Player.ONE : Player.TWO;
            }

            if (winner == Player.ONE) {
                p1Cards.addLast(p1Card);
                p1Cards.addLast(p2Card);
            } else {
                p2Cards.addLast(p2Card);
                p2Cards.addLast(p1Card);
            }
            deckConfigurations.add(currentDeckConfig);
        }

        return p1Cards.isEmpty() ? Player.TWO : Player.ONE;
    }

    private static int part2() {
        Deque<Integer> p1Cards = new ArrayDeque<>(player1Cards);
        Deque<Integer> p2Cards = new ArrayDeque<>(player2Cards);

        playRecursive(p1Cards, p2Cards);
        return calculateWinnersScore(p1Cards, p2Cards);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            readInput(filename);

            System.out.println("Part 1: " + part1());
            System.out.println("Part 2: " + part2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
