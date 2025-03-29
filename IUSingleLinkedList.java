import java.lang.reflect.InaccessibleObjectException;
import java.util.ConcurrentModificationException;
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

	public IUSingleLinkedList() {
		this.head = this.tail = null;
		this.size = 0;
		this.modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> node = new Node<T>(element);
		if (isEmpty()) {
			this.head = this.tail = node;
		} else {
			node.setNext(this.head);
			this.head = node;
		}
		this.size++;
		this.modCount++;
	}

	@Override
	public void addToRear(T element) {
		if (isEmpty()) {
			Node<T> node = new Node<T>(element);
			this.head = this.tail = node;

		} else {
			Node<T> node = new Node<T>(element);
			this.tail.setNext(node);
			this.tail = node;
		}
		this.size++;
		this.modCount++;

	}

	@Override
	public void add(T element) {
		addToRear(element);

	}

	@Override
	public void addAfter(T element, T target) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		Node<T> currNode = this.head;

		while (currNode != null) {
			if (currNode.getElement().equals(target)) {
				Node<T> node = new Node<>(element);
				node.setNext(currNode.getNext());
				currNode.setNext(node);

				if (currNode == this.tail) {
					this.tail = node;
				}

				this.size++;
				this.modCount++;
				return;
			}
			currNode = currNode.getNext();
		}

		throw new NoSuchElementException("Target element not found in the list.");
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > this.size) {
			throw new IndexOutOfBoundsException();
		}

		Node<T> node = new Node<>(element);

		if (index == 0) {
			node.setNext(this.head);
			this.head = node;
			if (this.size == 0) {
				this.tail = node;
			}
		} else {
			Node<T> currNode = this.head;
			for (int i = 0; i < index - 1; i++) {
				currNode = currNode.getNext();
			}

			node.setNext(currNode.getNext());
			currNode.setNext(node);

			if (node.getNext() == null) {
				this.tail = node;
			}
		}

		this.size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T first = this.head.getElement();
		this.head = this.head.getNext();

		if (this.head == null) {
			this.tail = null;
		}

		this.size--;
		this.modCount++;
		return first;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		if (this.size == 1) {
			return removeFirst();
		}

		Node<T> curr = this.head;
		while (curr.getNext() != this.tail) {
			curr = curr.getNext();
		}

		T last = this.tail.getElement();
		this.tail = curr;
		this.tail.setNext(null);

		this.size--;
		modCount++;
		return last;
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		boolean found = false;
		Node<T> previous = null;
		Node<T> current = this.head;

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

		if (this.size() == 1) {
			this.head = this.tail = null;
		} else if (current == this.head) {
			this.head = current.getNext();
		} else if (current == this.tail) {
			this.tail = previous;
			this.tail.setNext(null);
		} else {
			previous.setNext(current.getNext());
		}

		this.size--;
		modCount++;

		return current.getElement();
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		T element = this.head.getElement();
		if (index == 0) {
			removeFirst();
		} else {
			Node<T> currentNode = this.head;
			for (int i = 0; i < index - 1; i++) {
				currentNode = currentNode.getNext();
			}
			Node<T> toRemove = currentNode.getNext();
			element = toRemove.getElement();
			currentNode.setNext(toRemove.getNext());
			if (toRemove == this.tail) {
				this.tail = currentNode;
			}
			this.size--;
			modCount++;
		}
		return element;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> current = this.head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		current.setElement(element);
		this.modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= this.size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> current = this.head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		Node<T> current = this.head;
		int index = 0;

		while (current != null) {
			if (current.getElement().equals(element)) {
				return index;
			}
			current = current.getNext();
			index++;
		}

		return -1;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException("List is empty.");
		}
		return this.head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException("List is empty.");
		}
		return this.tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		Node<T> current = this.head;

		while (current != null) {
			if (current.getElement().equals(target)) {
				return true;
			}
			current = current.getNext();
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		if (this.size == 0) {
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "[]";
		}

		String result = "[";
		Node<T> current = this.head;

		while (current != null) {
			result += current.getElement();
			if (current.getNext() != null) {
				result += ", ";
			}
			current = current.getNext();
		}

		result += "]";
		return result;
	}

	@Override
	public Iterator<T> iterator() {
		return new IUSllIterator();
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
	private class IUSllIterator implements Iterator<T> {
		private Node<T> nextNode;
		private Node<T> lastReturned;
		private int expectedModCount;

		public IUSllIterator() {
			nextNode = head;
			lastReturned = null;
			expectedModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			return nextNode != null;
		}

		@Override
		public T next() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			lastReturned = nextNode;
			nextNode = nextNode.getNext();
			return lastReturned.getElement();
		}

		@Override
		public void remove() {
			if (modCount != expectedModCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			IUSingleLinkedList.this.remove(lastReturned.getElement());
			lastReturned = null;
			expectedModCount++;

		}

	}
}