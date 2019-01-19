package io.github.ficcitong.kvstore;

/**
 * InnerNode implementation of the B+ Tree. Each inner node has a parent node and a list of clildren
 * nodes.
 */
class InnerNode extends Node {
  // A list of children nodes
  private Object[] children;

  // Constructor
  public InnerNode() {
    this.elements = new String[ORDER + 1];
    this.children = new Object[ORDER + 2];
  }

  public Node getChild(int index) {
    return (Node) this.children[index];
  }

  /**
   * Sets the child at the desired index.
   * 
   * @param index at which index to insert
   * @param child a child node
   */
  public void setChild(int index, Node child) {
    this.children[index] = child;
    if (child != null) {
      child.setParent(this);
    }
  }

  @Override
  public NodeType getNodeType() {
    return NodeType.InnerNode;
  }

  /**
   * The Search function to find the index of the subtree that containing the key.
   * 
   * @param key the string key to be located
   * @return int index
   */
  @Override
  public int search(String key) {
    int index = 0;
    for (index = 0; index < this.getElementCount(); ++index) {
      // Return the index at which the key is larger
      int cpr = ((String) this.getElement(index)).toLowerCase().compareTo(key.toLowerCase());
      if (cpr == 0) {
        return index + 1;
      } else if (cpr > 0) {
        return index;
      }
    }
    return index;
  }

  /**
   * Inserts the key at the index and sets the correct children.
   * 
   * @param index      at where the key is inserted
   * @param key        the key to be inserted
   * @param leftChild  the left child node of the key
   * @param rightChild the right child node of the key
   */
  private void insertAt(int index, String key, Node leftChild, Node rightChild) {
    // Move back one slot for the new key
    for (int i = this.getElementCount() + 1; i > index; --i) {
      this.setChild(i, this.getChild(i - 1));
    }
    for (int i = this.getElementCount(); i > index; --i) {
      this.setElement(i, this.getElement(i - 1));
    }

    // Insert the new key
    this.setElement(index, key);
    this.setChild(index, leftChild);
    this.setChild(index + 1, rightChild);
    this.elementCount += 1;
  }

  /**
   * Split the current node and link the new node.
   * 
   * @return an InnerNode object that representing the right node after splitting.
   */
  @Override
  protected InnerNode split() {
    // Get the index of the middle element.
    int midIndex = this.getElementCount() / 2;

    InnerNode newNode = new InnerNode();

    // Move the keys to the new InnerNode
    for (int i = midIndex + 1; i < this.getElementCount(); ++i) {
      newNode.setElement(i - midIndex - 1, this.getElement(i));
      this.setElement(i, null);
    }

    // Move the children to the new InnerNode
    for (int i = midIndex + 1; i <= this.getElementCount(); ++i) {
      newNode.setChild(i - midIndex - 1, this.getChild(i));
      newNode.getChild(i - midIndex - 1).setParent(newNode);
      this.setChild(i, null);
    }

    this.setElement(midIndex, null);

    // Set the element counts of the new nodes
    newNode.elementCount = this.getElementCount() - midIndex - 1;
    this.elementCount = midIndex;

    return newNode;
  }

  /**
   * Solve the overflow of an inner node.
   * 
   * @return the node returned by pushKeyUp method.
   */
  @Override
  protected Node solveOverflow() {
    Node newNode = this.split();

    // If this is the parent node, create a new parent node.
    if (this.getParent() == null) {
      this.setParent(new InnerNode());
    }
    newNode.setParent(this.getParent());

    // Push up the middle key to parent node
    int midIndex = this.getElementCount() / 2;
    String upKey = (String) this.getElement(midIndex);

    InnerNode newInnerNode = (InnerNode) this.getParent();

    return newInnerNode.pushKeyUp(upKey, this, newNode);
  }


  /**
   * The method to push the middle key up.
   * 
   * @param key       the middle key to be pushed up
   * @param leftNode  the left node after split
   * @param rightNode the right node after split
   * @return the node after inserting or null if parent does not exist.
   */
  protected Node pushKeyUp(String key, Node leftNode, Node rightNode) {
    // Find the target position of the new key
    int index = this.search(key);

    // Insert the new key
    this.insertAt(index, key, leftNode, rightNode);

    // Check whether current node need to split
    if (this.isOverflow()) {
      return this.solveOverflow();
    } else {
      return this.getParent() == null ? this : null;
    }
  }

}
