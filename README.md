# Optimized Binary Search (Java)

A small, dependency-free Java project that implements and tests a boundary-aware binary search for sorted integer arrays, then compares it with a conventional binary search.

## Features

- Iterative binary search: **O(log n)** time and **O(1)** extra space.
- Checks the array range and its two boundary values before searching the interior.
- Returns the matching index, or `-1` when the target is absent.
- Handles empty arrays and duplicate values.
- Uses an overflow-safe midpoint calculation.
- Includes correctness tests and a repeatable comparison harness.

## Requirements

- JDK 8 or newer.
- No external libraries or build tool required.

## Project structure

| File | Purpose |
|---|---|
| `OptimizedBinarySearch.java` | Boundary-aware binary search implementation |
| `BinarySearchTest.java` | Dependency-free correctness tests |
| `BinarySearchExistingBenchmark.java` | Comparison harness for standard and optimized search |

## Compile and run

Compile the implementation and tests:

```bash
javac OptimizedBinarySearch.java BinarySearchTest.java
```

Run the tests:

```bash
java BinarySearchTest
```

Expected output:

```text
All tests passed (... assertions).
```

## Use it in your code

```java
int[] numbers = { 2, 4, 6, 8, 10, 12 };

int index = OptimizedBinarySearch.search(numbers, 8);

if (index >= 0) {
    System.out.println("Found at index: " + index);
} else {
    System.out.println("Not found");
}
```

**Important:** The input array must already be sorted in ascending order. The method does not sort the array.

## Run the benchmark

Compile:

```bash
javac OptimizedBinarySearch.java BinarySearchExistingBenchmark.java
```

Run with defaults (7 measured rounds and 100,000 searches per round):

```bash
java BinarySearchExistingBenchmark
```

You can change the measured rounds and searches per round:

```bash
java BinarySearchBenchmark 9 200000
```

The harness:
- warms up both implementations;
- alternates which implementation runs first;
- times a batch of searches instead of timing every individual call;
- uses the median of repeated measurements;
- checks that both methods agree on whether each target exists;
- includes both successful and unsuccessful searches.

Benchmark results depend on the JVM, CPU, memory, operating system, and workload. This small harness is useful for experimentation, but it is not a rigorous substitute for [JMH](https://github.com/openjdk/jmh), the Java Microbenchmark Harness. Do not treat one run as proof that one implementation is always faster.

## Complexity

| Operation | Time complexity | Extra space |
|---|---:|---:|
| Standard binary search | O(log n) | O(1) |
| Boundary-aware binary search | O(log n) | O(1) |

The boundary checks can help when targets are often at the beginning/end or outside the array's value range. For uniformly distributed targets, the extra checks may not improve performance. Benchmark with the data distribution that matches your actual use case.

## License

No license has been specified yet. Add a license file before others reuse or redistribute this project.
