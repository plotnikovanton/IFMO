package task1_reflection;

interface Heap<E extends Comparable<E>> {
  void add(E elem);   // Добавляет элемент в кучу.
  E first();          // Выдает элемент из кучи с максимальным 
                      // приоритетом (наименьший!), не удаляя его
  boolean contains(E elem); // Проверяет, есть ли элемент в куче.
  E retrieve();       // Удаляет из кучи элемент с максимальным 
                      // приоритетом и выдает его в качестве результата.
}