package task1_reflection;

public class Main {
    public static void main(String[] args) {
        Benchmark sortedHeapTest = new Benchmark("task1_reflection.SortedHeap", 10000, 12345);
        Benchmark binHeapTest = new Benchmark("task1_reflection.BinHeap", 10000, 12345);

    }
}
