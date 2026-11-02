import java.util.Arrays;

/**
 * Dependency-free correctness tests.
 * Run with: javac OptimizedBinarySearch.java BinarySearchTest.java && java BinarySearchTest
 */
public class BinarySearchTest {

    private static int assertions = 0;

    private static void assertFound(int[] arr, int target) {
        int index = OptimizedBinarySearch.search(arr, target);
        check(index >= 0 && index < arr.length,
                "Expected to find " + target + " in " + Arrays.toString(arr));
        check(arr[index] == target,
                "Returned index does not contain target " + target);
    }

    private static void assertMissing(int[] arr, int target) {
        check(OptimizedBinarySearch.search(arr, target) == -1,
                "Expected " + target + " to be absent");
    }

    private static void check(boolean condition, String message) {
        assertions++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void testEmptyAndSingleElementArrays() {
        check(OptimizedBinarySearch.search(new int[0], 10) == -1,
                "Empty array should return -1");

        assertFound(new int[] { 7 }, 7);
        assertMissing(new int[] { 7 }, 6);
        assertMissing(new int[] { 7 }, 8);
    }

    private static void testBoundariesAndMiddle() {
        int[] arr = { 2, 4, 6, 8, 10, 12, 14 };

        assertFound(arr, 2);
        assertFound(arr, 8);
        assertFound(arr, 14);
        assertMissing(arr, 1);
        assertMissing(arr, 3);
        assertMissing(arr, 15);
    }

    private static void testDuplicates() {
        int[] arr = { 1, 2, 2, 2, 3, 4, 4 };
        assertFound(arr, 1);
        assertFound(arr, 2);
        assertFound(arr, 3);
        assertFound(arr, 4);
        assertMissing(arr, 5);
    }

    private static void testNegativeAndExtremeValues() {
        int[] arr = {
                Integer.MIN_VALUE, -100, -1, 0, 1, 100, Integer.MAX_VALUE
        };

        assertFound(arr, Integer.MIN_VALUE);
        assertFound(arr, -1);
        assertFound(arr, 0);
        assertFound(arr, Integer.MAX_VALUE);
        assertMissing(arr, Integer.MIN_VALUE + 1);
        assertMissing(arr, Integer.MAX_VALUE - 1);
    }

    private static void testNullArray() {
        boolean thrown = false;
        try {
            OptimizedBinarySearch.search(null, 1);
        } catch (NullPointerException expected) {
            thrown = true;
        }
        check(thrown, "Null input should throw NullPointerException");
    }

    public static void main(String[] args) {
        testEmptyAndSingleElementArrays();
        testBoundariesAndMiddle();
        testDuplicates();
        testNegativeAndExtremeValues();
        testNullArray();

        System.out.println("All tests passed (" + assertions + " assertions).");
    }
}
