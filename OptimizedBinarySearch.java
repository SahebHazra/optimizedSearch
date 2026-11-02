public class OptimizedBinarySearch {
    public static int search(int arr[], int target) {
        int low = 0;
        int high = arr.length - 1;
        if (high < 0 || arr[low] > target || arr[high] < target)
            return -1;

        while (low <= high) {
            if (arr[low] == target)
                return low;
            if (arr[high] == target)
                return high;

            int mid = (low + high) / 2;
            if (arr[mid] == target)
                return mid;

            if (arr[mid] < target)
                low = mid + 1;
            else
                high = mid - 1;
        }
        return -1;
    }
}
