
import java.util.Random;

public class BinarySearchExistingBenchmark {

    // ---------- VALUE INITIALIZATION ----------

    static int[] initializeArray(int size) {
        int[] arr = new int[size];

        for (int i = 0; i < size; i++) {
            arr[i] = i * 2;
        }

        return arr;
    }

    static int[] initializeTargets(int[] arr, int tests) {
        int[] targets = new int[tests];
        Random random = new Random(42);

        for (int i = 0; i < tests; i++) {
            int randomIndex = random.nextInt(arr.length);
            targets[i] = arr[randomIndex];
        }

        return targets;
    }

    // ---------- STANDARD BINARY SEARCH ----------

    static int standardSearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (arr[mid] == target)
                return mid;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    // ---------- OPTIMIZED BINARY SEARCH ----------

    static int optimizedSearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        if (high < 0 || arr[low] > target || arr[high] < target)
            return -1;

        while (low <= high) {
            if (arr[low] == target)
                return low;

            if (arr[high] == target)
                return high;

            int mid = low + (high - low) / 2;

            if (arr[mid] == target)
                return mid;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }

        return -1;
    }

    // ---------- BENCHMARK ----------

    static void runBenchmark(int[] arr, int[] targets) {

        long standardTime = 0;
        long optimizedTime = 0;

        int tests = targets.length;

        // Warm-up
        for (int i = 0; i < 20_000; i++) {
            int target = targets[i % tests];
            optimizedSearch(arr, target);
            standardSearch(arr, target);
        }

        // Actual test
        for (int i = 0; i < tests; i++) {

            int target = targets[i];

            long start = System.nanoTime();
            int a = standardSearch(arr, target);
            standardTime += System.nanoTime() - start;

            start = System.nanoTime();
            int b = optimizedSearch(arr, target);
            optimizedTime += System.nanoTime() - start;

            if (a != b || a == -1) {
                throw new AssertionError(
                        "Incorrect result for target: " + target);
            }
        }

        System.out.println("Array size: " + arr.length);
        System.out.println("Tests: " + tests);

        System.out.println(
                "Standard: " + standardTime / 1_000_000.0 + " ms");

        System.out.println(
                "Optimized: " + optimizedTime / 1_000_000.0 + " ms");

        System.out.println();
    }

    // ---------- MAIN ----------

    public static void main(String[] args) {

        int[] sizes = {
                1,
                10,
                100,
                10_000,
                100_000,
                1_000_000,
                10_000_000,
                100_000_000
        };

        int tests = 1_000_000;

        for (int size : sizes) {

            int[] arr = initializeArray(size);
            int[] targets = initializeTargets(arr, tests);

            runBenchmark(arr, targets);
        }
    }
}
