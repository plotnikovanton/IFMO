package task1_reflection;

import java.util.Arrays;

public class BinHeap<E extends Comparable<E>> implements Heap<E> {
    private Comparable[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public BinHeap(int n) {
        this.array = new Comparable[n];
        this.size = 0;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    private void bubbleDown() {
        int index = 1;

        // bubble down
        while (hasLeftChild(index)) {
            int smallerChild = leftIndex(index);

            if (hasRightChild(index)
                    && array[leftIndex(index)].compareTo(array[rightIndex(index)]) > 0) {
                smallerChild = rightIndex(index);
            }

            if (array[index].compareTo(array[smallerChild]) > 0) {
                swap(index, smallerChild);
            } else {
                break;
            }
            index = smallerChild;
        }
    }

    private void bubbleUp() {
        int index = this.size;

        while (hasParent(index)
                && (parent(index).compareTo((E)array[index]) > 0)) {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private boolean hasParent(int i) {
        return i > 1;
    }

    private int leftIndex(int i) {
        return i * 2;
    }

    private int rightIndex(int i) {
        return i * 2 + 1;
    }

    private boolean hasLeftChild(int i) {
        return leftIndex(i) <= size;
    }

    private boolean hasRightChild(int i) {
        return rightIndex(i) <= size;
    }

    private E parent(int i) {
        return (E)array[parentIndex(i)];
    }

    private int parentIndex(int i) {
        return i / 2;
    }

    private Comparable[] resize() {
        return Arrays.copyOf(array, array.length * 2);
    }

    private void swap(int index1, int index2) {
        E tmp = (E)array[index1];
        array[index1] = array[index2];
        array[index2] = tmp;
    }

    @Override
    public void add(E elem) {
        if (size >= array.length - 1) {
            array = this.resize();
        }

        size++;
        int index = size;
        array[index] = elem;

        bubbleUp();

    }

    @Override
    public E first() {
        if (this.isEmpty()) {
            throw new IllegalStateException();
        }

        return (E)array[1];
    }

    @Override
    public boolean contains(E elem) {
        for (int i = 0; i < array.length-1 && array[i] != null; i++){
            if (elem.equals((E)array[i])){
                return true;
            }
        }
        return false;
    }

    @Override
    public E retrieve() {
        E result = first();

        array[1] = array[size];
        array[size] = null;
        size--;

        bubbleDown();

        return result;
    }
}
