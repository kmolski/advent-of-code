public class ComboBreaker {
    private static int getLoopCountForKey(int subject, int target) {
        int i = 0;
        for (long result = 1L; result != target; ++i) {
            result *= subject;
            result %= 2020_12_27;
        }
        return i;
    }

    private static long transformSubjectNumber(int subject, int loopSize) {
        long result = 1L;
        for (int i = 0; i < loopSize; ++i) {
            result *= subject;
            result %= 2020_12_27;
        }
        return result;
    }

    private static long part1(int key1, int key2) {
        int loopSize1 = getLoopCountForKey(7, key1);
        int loopSize2 = getLoopCountForKey(7, key2);

        long encryptionKey1 = transformSubjectNumber(key1, loopSize2);
        long encryptionKey2 = transformSubjectNumber(key2, loopSize1);

        if (encryptionKey1 != encryptionKey2) {
            throw new AssertionError("Encryption keys are not equal!");
        }

        return encryptionKey1;
    }

    public static void main(String[] args) {
        System.out.println("Part 1: " + part1(13233401, 6552760));
    }
}
