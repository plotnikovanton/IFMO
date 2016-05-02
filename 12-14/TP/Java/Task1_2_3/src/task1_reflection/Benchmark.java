package task1_reflection;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class Benchmark {
    private static final int MAX_RND = 1000;
    private Heap<Integer> heap;
    private Random random;
    @SuppressWarnings("unchecked")
    public Benchmark(String className, int n, long seed) {
        this.random = new Random(seed);
        try {
            Class heapClass = Class.forName(className);
            heap = (Heap) heapClass.getConstructor(int.class).newInstance(n);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------------------------" +
                           "task1_reflection.Benchmark for " + className +
                           "----------------------------------------------");

        long time;
        time = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            heap.add(random.nextInt(MAX_RND));
        }
        System.out.println("Time for add "+n+" elements: "+(System.currentTimeMillis()-time)+"mills.");

        time = System.currentTimeMillis();
        heap.contains(20);
        System.out.println("Time for find element: "+(System.currentTimeMillis()-time)+"mills.");
    }
}
