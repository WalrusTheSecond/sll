import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * An array based implementation of IndexedUnsortedList interface
 * 
 * @param <T> - type of elements held in this collection
 * @author Evan Wallace
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
    private T[] array;
    private int rear;
    private int modCount;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Helper method to double the capacity of arrays
     */
    private void arrayCapacityDoubler(T[] list) {
        int newArrayCapacity = list.length * 2;
        this.array = (T[]) new Object[newArrayCapacity];
        for (int i = 0; i < this.rear; i++) {
            this.array[i] = list[i];
        }
    }

    /**
     * Default constructor
     * creates an array of size DEFAULT_CAPACITY which is 10
     * sets size = 0
     */
    public IUArrayList() {
        this.array = (T[]) new Object[DEFAULT_CAPACITY];
        this.rear = 0;
    }

    /**
     * Custom Capacity Constructor
     * 
     * @param capacity the capacity of the created arrayList
     *                 sets size = 0
     */
    public IUArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }
        this.array = (T[]) new Object[capacity];
        this.rear = 0;
    }

    public void addToFront(T element) {
        if (this.rear == this.array.length) {
            arrayCapacityDoubler(this.array);
        }
        T hold;
        for (int i = this.rear; i > 0; i--) {
            this.array[i] = this.array[i - 1];
        }
        this.array[0] = element;
        // update counters
        this.rear++;
        this.modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (this.rear == this.array.length) {
            arrayCapacityDoubler(this.array);
        }
        this.array[this.rear] = element;
        // update counters
        this.rear++;
        this.modCount++;
    }

    @Override
    public void add(T element) {
        addToFront(element);
    }

    @Override
    public void addAfter(T element, T target) {
        int targetIndex = -1;
        for (int i = 0; i < this.rear; i++) {
            if (this.array[i].equals(target)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex == -1) {
            throw new NoSuchElementException("Target element not found");
        }
        if (this.rear == this.array.length) {
            arrayCapacityDoubler(this.array);
        }
        for (int j = this.rear; j > targetIndex + 1; j--) {
            this.array[j] = this.array[j - 1];
        }
        this.array[targetIndex + 1] = element;
        // update counters
        this.modCount++;
        this.rear++;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > this.rear) {
            throw new IndexOutOfBoundsException();
        }
        if (this.rear == this.array.length) {
            arrayCapacityDoubler(this.array);
        }
        if (index == 0 && this.rear == 0) {
            this.array[0] = element;
        }
        for (int i = this.rear; i > index; i--) {
            this.array[i] = this.array[i - 1];
        }
        this.array[index] = element;
        // update counters
        this.modCount++;
        this.rear++;
    }

    @Override
    public T removeFirst() {
        if (this.rear == 0) {
            throw new NoSuchElementException();
        }
        T returnValue = this.array[0];
        for (int i = 0; i < this.rear - 1; i++) {
            this.array[i] = this.array[i + 1];
        }
        // update counters
        this.modCount++;
        this.rear--;
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (this.rear == 0) {
            throw new NoSuchElementException();
        }
        T returnValue = this.array[this.rear - 1];
        this.array[this.rear - 1] = null;
        // update counters
        this.modCount++;
        this.rear--;
        return returnValue;
    }

    @Override
    public T remove(T element) {
        // Locate element
        int targetElement = -1;
        for (int i = 0; i < this.rear; i++) {
            if (this.array[i].equals(element)) {
                targetElement = i;
                break;
            }
        }
        if (targetElement < 0) {
            throw new NoSuchElementException();
        }
        T returnValue = this.array[targetElement];
        for (int i = targetElement; i < this.rear - 1; i++) {
            this.array[i] = this.array[i + 1];
        }
        this.array[this.rear - 1] = null;
        // update counters
        this.rear--;
        this.modCount++;
        return returnValue;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= this.rear) {
            throw new IndexOutOfBoundsException();
        }
        T returnValue = this.array[index];
        for (int i = index; i < this.rear - 1; i++) {
            this.array[i] = this.array[i + 1];
        }
        this.array[this.rear - 1] = null;
        // update counters
        this.rear--;
        this.modCount++;
        return returnValue;
    }

    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= this.rear) {
            throw new IndexOutOfBoundsException();
        }
        this.array[index] = element;
        // update counters
        this.modCount++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.rear) {
            throw new IndexOutOfBoundsException();
        }
        return this.array[index];
    }

    @Override
    public int indexOf(T element) {
        for (int i = 0; i < this.rear; i++) {
            if (this.array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public T first() {
        if (this.rear == 0) {
            throw new NoSuchElementException();
        }
        return this.array[0];
    }

    @Override
    public T last() {
        if (this.rear == 0) {
            throw new NoSuchElementException();
        }
        return this.array[this.rear - 1];
    }

    @Override
    public boolean contains(T target) {
        for (int i = 0; i < this.rear; i++) {
            if (this.array[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        if (this.rear == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return this.rear;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.array);
    }

    @Override
    public Iterator<T> iterator() {

        return new IUArrayIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("listIterator() is not implemented");
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        throw new UnsupportedOperationException("listIterator() is not implemented");
    }

    /**
     * An iterator over an array base colleciton
     * 
     * @param <T> the type of elements returned by this iterator
     */
    private class IUArrayIterator implements Iterator<T> {
        private int curIndex;
        private int lastReturnedIndex = -1;
        private int expectedModCount;

        public IUArrayIterator() {
            this.curIndex = 0;
            this.expectedModCount = modCount;
        }

        private void checkForConcurrentModification() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        public boolean hasNext() {
            checkForConcurrentModification();
            return curIndex < rear;
        }

        public T next() {
            checkForConcurrentModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = array[curIndex];
            lastReturnedIndex = curIndex;
            curIndex++;
            return element;
        }

        @Override
        public void remove() {
            checkForConcurrentModification();
            if (lastReturnedIndex < 0) {
                throw new IllegalStateException("next() has not been called or remove() was called after the last call to next()");
            }
            for (int i = lastReturnedIndex; i < rear - 1; i++) {
                array[i] = array[i + 1];
            }
            array[rear - 1] = null;
            rear--;
            curIndex = lastReturnedIndex;
            lastReturnedIndex = -1;
            modCount++;
            expectedModCount++;
        }
    }
}