/**
 * Boundary-aware binary search for an ascending, sorted int array.
 *
 * Returns the index of target, or -1 when target is not present.
 * The array must be sorted in ascending order.
 */
public final class OptimizedBinarySearch {

    private OptimizedBinarySearch() {
        // Utility class.
    }

    public static int search(int[] arr, int target) {
        if (arr == null) {
            throw new NullPointerException("arr must not be null");
        }

        int length = arr.length;
        if (length == 0 || target < arr[0] || target > arr[length - 1]) {
            return -1;
        }

        // Handle the boundaries once, rather than checking them every loop.
        if (arr[0] == target) {
            return 0;
        }
        if (arr[length - 1] == target) {
            return length - 1;
        }

        int low = 1;
        int high = length - 2;

        while (low <= high) {
            // This form avoids overflow that can occur with (low + high) / 2.
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
}
