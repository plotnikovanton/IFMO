package task1_reflection;

public class SortedHeap<E extends Comparable<E>> implements Heap<E> {
    private Comparable array[];
    private int last = 0;
    private int first = 0;
    private int size = 0;

    public SortedHeap(int n) {
        this.array = new Comparable[n];
    }
    @SuppressWarnings("unchecked")
    @Override
    public void add(E elem) {
        if (size == array.length) {
            throw new IndexOutOfBoundsException();
        } else if (size == 0){
            array[0] = elem;
            last = 0;
            first = 0;
            size = 1;
        } else {
            //Чудеса с тернарными операторами
            size++;
            int i;
            if (last == array.length-1){
                i = 0;
                last = 0;
            } else {
                i = last++;
            }
            while (elem.compareTo((E)array[i]) < 0) {
                array[i==array.length-1 ? 0 : i+1] = array[i];
                i = i - 1 == -1 ? array.length - 1 : i - 1;
                if (i== (first == 0 ? array.length-1 : first-1)) {
                    break;
                }
            }
            array[i+1==array.length ? 0 : i+1] = elem;
        }
    }

    @Override
    public E first() {
        return (E)array[first];
    }

    @Override
    public boolean contains(E elem) {
        //TODO binary search
        for (Comparable i : array) {
            if (elem.equals((E)i)){
                return true;
            }
        }
        return false;
    }

    @Override
    public E retrieve() {
        size--;
        E toReturn = (E)array[first];
        first = first == array.length - 1 ? 0 : first + 1;
        return toReturn;
    }
}
