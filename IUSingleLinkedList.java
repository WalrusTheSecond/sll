import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size, modCount;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		if(isEmpty()){
            Node<T> node = new Node<T>(element);
            head = tail = node;

        } else {
            Node<T> node = new Node<T>(element);
            node.setNext(head);
            head = node;
        } 
        size++;
        modCount++;
	}

	@Override
	public void addToRear(T element) {
		if(isEmpty()){
            Node<T> node = new Node<T>(element);
            head = tail = node;

        } else {
            Node<T> node = new Node<T>(element);
            tail.setNext(node);
            tail = node;
        } 
        size++;
        modCount++;
		
	}

	@Override
	public void add(T element) {
		addToRear(element);
		
	}

	@Override
	public void addAfter(T element, T target) {
        Node<T> currNode = head;
        int count = 0;
    
        if (isEmpty()){
            throw new NoSuchElementException();
        }
		while(count < size) {
            if(currNode == target){
                Node<T> node = new Node<T>(element);
                node.setNext(currNode.getNext());
                
            }
            count++;
        }

	}

	@Override
	public void add(int index, T element) {
		// TODO 
		
	}

	@Override
	public T removeFirst() {
		// TODO 
		return null;
	}

	@Override
	public T removeLast() {
		// TODO 
		return null;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previous = null;
		Node<T> current = head;
		
		while (current != null && !found) {
			if (element.equals(current.getElement())) {
				found = true;
			} else {
				previous = current;
				current = current.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (current == head) { //first node
			head = current.getNext();
		} else if (current == tail) { //last node
			tail = previous;
			tail.setNext(null);
		} else { //somewhere in the middle
			previous.setNext(current.getNext());
		}
		
		size--;
		modCount++;
		
		return current.getElement();
	}

	@Override
	public T remove(int index) {
		// TODO 
		return null;
	}

	@Override
	public void set(int index, T element) {
		// TODO 
		
	}

	@Override
	public T get(int index) {
		// TODO 
		return null;
	}

	@Override
	public int indexOf(T element) {
		// TODO 
		return 0;
	}

	@Override
	public T first() {
		// TODO 
		return null;
	}

	@Override
	public T last() {
		// TODO 
		return null;
	}

	@Override
	public boolean contains(T target) {
		// TODO 
		return false;
	}

	@Override
	public boolean isEmpty() {
		if(size == 0) {
            return true;
        }
		return false;
	}

	@Override
	public int size() {
		// TODO 
		return 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if(nextNode.getNext() != null){
                return true;
            }
			else return false;
		}

		@Override
		public T next() {
			// TODO 
			return null;
		}
		
		@Override
		public void remove() {
			// TODO
		}
	}
}