import java.util.Arrays;
import java.util.Random;

/**
 * A lightweight comparison harness, not a replacement for JMH.
 *
 * Run: javac OptimizedBinarySearch.java BinarySearchBenchmark.java
 *      java BinarySearchBenchmark
 *
 * Optional arguments: number of measured rounds, number of searches per round.
 */
public class BinarySearchBenchmark {

    private static volatile long blackhole;

    private static int[] createArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i * 2;
        }
        return arr;
    }

    private static int[] createTargets(int[] arr, int count) {
        int[] targets = new int[count];
        Random random = new Random(42);

        for (int i = 0; i < count; i++) {
            int index = random.nextInt(arr.length);
            // Half successful searches, half unsuccessful searches.
            targets[i] = (i & 1) == 0 ? arr[index] : arr[index] + 1;
        }
        return targets;
    }

    private static int standardSearch(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int value = arr[mid];

            if (value == target) {
                return mid;
            }
            if (value < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    private static long runStandard(int[] arr, int[] targets) {
        long checksum = 0;
        for (int target : targets) {
            checksum += standardSearch(arr, target);
        }
        return checksum;
    }

    private static long runOptimized(int[] arr, int[] targets) {
        long checksum = 0;
        for (int target : targets) {
            checksum += OptimizedBinarySearch.search(arr, target);
        }
        return checksum;
    }

    private static long timeStandard(int[] arr, int[] targets) {
        long start = System.nanoTime();
        blackhole ^= runStandard(arr, targets);
        return System.nanoTime() - start;
    }

    private static long timeOptimized(int[] arr, int[] targets) {
        long start = System.nanoTime();
        blackhole ^= runOptimized(arr, targets);
        return System.nanoTime() - start;
    }

    private static long median(long[] values) {
        long[] copy = values.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }

    private static void verifyResults(int[] arr, int[] targets) {
        for (int target : targets) {
            int standard = standardSearch(arr, target);
            int optimized = OptimizedBinarySearch.search(arr, target);

            if (standard != -1 && arr[standard] != target) {
                throw new AssertionError("Standard search returned a bad index");
            }
            if (optimized != -1 && arr[optimized] != target) {
                throw new AssertionError("Optimized search returned a bad index");
            }
            if ((standard == -1) != (optimized == -1)) {
                throw new AssertionError("Search methods disagree for " + target);
            }
        }
    }

    private static void benchmark(int size, int rounds, int testsPerRound) {
        int[] arr = createArray(size);
        int[] targets = createTargets(arr, testsPerRound);
        verifyResults(arr, targets);

        // Warm both methods before collecting measurements.
        for (int i = 0; i < 4; i++) {
            runStandard(arr, targets);
            runOptimized(arr, targets);
        }

        long[] standardTimes = new long[rounds];
        long[] optimizedTimes = new long[rounds];

        // Reverse the execution order each round to reduce systematic order bias.
        for (int round = 0; round < rounds; round++) {
            if ((round & 1) == 0) {
                standardTimes[round] = timeStandard(arr, targets);
                optimizedTimes[round] = timeOptimized(arr, targets);
            } else {
                optimizedTimes[round] = timeOptimized(arr, targets);
                standardTimes[round] = timeStandard(arr, targets);
            }
        }

        long standardMedian = median(standardTimes);
        long optimizedMedian = median(optimizedTimes);
        double standardNsPerSearch = (double) standardMedian / testsPerRound;
        double optimizedNsPerSearch = (double) optimizedMedian / testsPerRound;
        double speedup = (double) standardMedian / optimizedMedian;

        System.out.printf("Array size: %,d | Searches per round: %,d%n",
                size, testsPerRound);
        System.out.printf("Standard median:  %.3f ms (%.2f ns/search)%n",
                standardMedian / 1_000_000.0, standardNsPerSearch);
        System.out.printf("Optimized median: %.3f ms (%.2f ns/search)%n",
                optimizedMedian / 1_000_000.0, optimizedNsPerSearch);
        System.out.printf("Speedup: %.2fx%n%n", speedup);
    }

    public static void main(String[] args) {
        int rounds = args.length > 0 ? Integer.parseInt(args[0]) : 7;
        int testsPerRound = args.length > 1
                ? Integer.parseInt(args[1]) : 100_000;

        if (rounds < 3 || testsPerRound < 1) {
            throw new IllegalArgumentException(
                    "Use at least 3 rounds and at least 1 search per round.");
        }

        int[] sizes = { 1, 10, 100, 10_000, 100_000, 1_000_000, 10_000_000 };
        for (int size : sizes) {
            benchmark(size, rounds, testsPerRound);
        }

        System.out.println("Checksum: " + blackhole);
        System.out.println("Note: use JMH for publishable JVM benchmark results.");
    }
}
