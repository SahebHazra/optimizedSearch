/**
 * Small manual example for OptimizedBinarySearch.
 */
public class test {
    public static void main(String[] args) {
        int[] numbers = { 1, 3, 5, 7, 9 };

        int target = 7;
        int index = OptimizedBinarySearch.search(numbers, target);

        if (index >= 0) {
            System.out.println(target + " found at index " + index);
        } else {
            System.out.println(target + " was not found");
        }
    }
}
