import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class OperationOrder {
    private static final Map<String, Integer> inversePrecedences = Map.ofEntries(
            new SimpleEntry<>("+", 2),
            new SimpleEntry<>("-", 2),
            new SimpleEntry<>("*", 1),
            new SimpleEntry<>("/", 1)
    );

    private static String[] readLines(String filename) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            return lines.toArray(String[]::new);
        }
    }

    private static Stream<String> tokenize(String equationString) {
        Pattern tokenPattern = Pattern.compile("(\\d+|[()+\\-*/])");
        Matcher matcher = tokenPattern.matcher(equationString);
        return matcher.results().map(MatchResult::group);
    }

    private static void parse(Stream<String> tokens, Map<String, Integer> precedences, Queue<String> outQueue) {
        Deque<String> opStack = new ArrayDeque<>();
        tokens.forEachOrdered(token -> {
            if (token.matches("\\d+")) {
                outQueue.add(token);
            } else if (token.matches("[+\\-*/]")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")
                        && (precedences == null || (precedences.get(opStack.peek())) > precedences.get(token))) {
                    outQueue.add(opStack.pop());
                }
                opStack.push(token);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.peek().equals("(")) {
                    outQueue.add(opStack.pop());
                }
                if (opStack.peek().equals("(")) { opStack.pop(); }
                else if (!opStack.isEmpty()) { outQueue.add(opStack.pop()); }
            }
        });

        while (!opStack.isEmpty()) { outQueue.add(opStack.pop()); }
    }

    private static long calculate(Queue<String> queue) {
        Deque<Long> variableStack = new ArrayDeque<>();
        while (!queue.isEmpty()) {
            String token = queue.remove();
            switch (token) {
                case "+": {
                    variableStack.push(variableStack.pop() + variableStack.pop());
                    break;
                }
                case "*": {
                    variableStack.push(variableStack.pop() * variableStack.pop());
                    break;
                }
                case "-": {
                    long a = variableStack.pop(), b = variableStack.pop();
                    variableStack.push(a - b);
                    break;
                }
                case "/": {
                    long a = variableStack.pop(), b = variableStack.pop();
                    variableStack.push(a / b);
                    break;
                }
                default: {
                    variableStack.push(Long.parseLong(token));
                    break;
                }
            }
        }

        return variableStack.pop();
    }

    private static LongStream solve(String[] equations, Map<String, Integer> precedences) {
        return Arrays.stream(equations).mapToLong(equationString -> {
            Queue<String> queue = new LinkedList<>();
            Stream<String> tokens = tokenize(equationString);
            parse(tokens, precedences, queue);
            return calculate(queue);
        });
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Input file name not found!");
        }

        try {
            String filename = args[0];
            String[] lines = readLines(filename);

            System.out.println("Part 1: " + solve(lines, null).sum());
            System.out.println("Part 2: " + solve(lines, inversePrecedences).sum());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
