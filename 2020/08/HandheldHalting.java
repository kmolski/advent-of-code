import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Console {
    private final String[] instructions;
    private int[] instructionExecCount;

    private int programCounter = 0;
    private int accumulator = 0;

    public Console(String program) {
        instructions = program.lines().toArray(String[]::new);
        instructionExecCount = new int[instructions.length];
    }

    private void reset() {
        instructionExecCount = new int[instructions.length];
        programCounter = 0;
        accumulator = 0;
    }

    public void simulate(int maxExecCount) {
        while (programCounter < instructions.length && ++instructionExecCount[programCounter] < maxExecCount) {
            String[] currentInstruction = instructions[programCounter].split(" ");

            switch (currentInstruction[0]) { // Instruction code
                case "nop":
                    ++programCounter;
                    break;
                case "acc":
                    accumulator += Integer.parseInt(currentInstruction[1]); // Instr. argument
                    ++programCounter;
                    break;
                case "jmp":
                    programCounter += Integer.parseInt(currentInstruction[1]); // Instr. argument
                    break;
            }
        }
    }

    public void fixProgram() {
        for (int i = 0; i < instructions.length; ++i) {
            reset();

            String[] instruction = instructions[i].split(" ");
            switch (instruction[0]) {
                case "nop":
                    instructions[i] = "jmp " + instruction[1];
                    simulate(1000);
                    if (programCounter == instructions.length) return; // Normal halt
                    instructions[i] = "nop " + instruction[1];
                    break;
                case "jmp":
                    instructions[i] = "nop " + instruction[1];
                    simulate(1000);
                    if (programCounter == instructions.length) return; // Normal halt
                    instructions[i] = "jmp " + instruction[1];
                    break;
                default:
                    break;
            }
        }

        throw new AssertionError("No instruction fix found!");
    }

    public int getAccumulatorValue() {
        return accumulator;
    }
}

public class HandheldHalting {
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
            Console handheldConsole = new Console(fileContents);

            handheldConsole.simulate(2);
            System.out.println("Part 1: " + handheldConsole.getAccumulatorValue());
            handheldConsole.fixProgram();
            System.out.println("Part 2: " + handheldConsole.getAccumulatorValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
