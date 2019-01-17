package io.github.ficcitong.kvstore;

abstract class Node {
  protected static final int ORDER = 2;

  protected Object[] elements;
  protected int elementCount;
  protected Node parent;

  public Object getElement(int index) {
    return this.elements[index];
  }

  public void setElement(int index, Object element) {
    this.elements[index] = element;
  }

  public int getElementCount() {
    return this.elementCount;
  }

  public Node getParent() {
    return this.parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public abstract NodeType getNodeType();

  public abstract int search(String key);

  protected abstract Node split();

  public boolean isOverflow() {
    return this.getElementCount() == this.elements.length;
  }

  protected abstract Node solveOverflow();
}
