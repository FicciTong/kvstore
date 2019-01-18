package io.github.ficcitong.kvstore;

/**
 * The abstract class Node that represent a general node in the B+ Tree. Will be extended to
 * InnerNode and LeafNode.
 */
abstract class Node {

  // The order of the nodes
  protected static final int ORDER = 4;

  // Elements represents String keys in a InnerNode, and KeyValuePairs in a LeafNode
  protected Object[] elements;
  protected int elementCount;
  protected Node parent;

  /**
   * Gets the element using index.
   * 
   * @param index the index in the element array
   * @return the element
   */
  public Object getElement(int index) {
    return this.elements[index];
  }

  /**
   * Sets the element using index.
   * 
   * @param index   the index in the element array
   * @param element the element
   */
  public void setElement(int index, Object element) {
    this.elements[index] = element;
  }

  public int getElementCount() {
    return this.elementCount;
  }

  /**
   * Gets the parent of current node.
   * 
   * @return the parent of current node
   */
  public Node getParent() {
    return this.parent;
  }

  /**
   * Sets the parent of current node.
   * 
   * @param parent the parent node of current node
   */
  public void setParent(Node parent) {
    this.parent = parent;
  }

  public abstract NodeType getNodeType();

  public abstract int search(String key);

  protected abstract Node split();

  /**
   * Determine whether the node will overflow with next insertion.
   * 
   * @return true if will overflow
   */
  public boolean isOverflow() {
    return this.getElementCount() == this.elements.length;
  }

  protected abstract Node solveOverflow();
}
