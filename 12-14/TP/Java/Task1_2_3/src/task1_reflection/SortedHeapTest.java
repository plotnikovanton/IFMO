package task1_reflection;

public class SortedHeapTest {
    @org.junit.Test
    public void testAdd() {
        //Плохие тесты без асертов, посмотреть дебагером чтобы как оно все работает
        Heap<Integer> heap = new SortedHeap<Integer>(5);
        heap.add(2);
        heap.add(5);
        heap.add(1);
        heap.add(3);
        heap.add(4);

        heap.retrieve();
        heap.add(0);
        heap.retrieve();
        heap.add(0);
        heap.retrieve();
        heap.add(0);
        heap.retrieve();
        heap.add(0);
        heap.retrieve();
        heap.add(0);
        heap.retrieve();
        heap.add(0);
        return;
    }
}
