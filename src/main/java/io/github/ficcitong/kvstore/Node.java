package io.github.ficcitong.kvstore;

abstract class Node<E> {
  protected static final int ORDER = 4;

  protected Object[] elements;
  protected int elementCount;
  protected Node<E> parent;

  @SuppressWarnings("unchecked")
  public E getElement(int index) {
    return (E) this.elements[index];
  }

  public void setElement(int index, E element) {
    this.elements[index] = element;
  }

  public int getElementCount() {
    return this.elementCount;
  }

  public Node<E> getParent() {
    return this.parent;
  }

  public void setParent(Node<E> parent) {
    this.parent = parent;
  }

  public abstract NodeType getNodeType();

  public abstract int search(String key);

  protected abstract Node<E> split();

  public boolean isOverflow() {
    return this.getElementCount() == this.elements.length;
  }

  protected abstract Node<E> solveOverflow();
}
