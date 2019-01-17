package io.github.ficcitong.kvstore;

class InnerNode extends Node {
  private Object[] children;

  public InnerNode() {
    this.elements = new String[ORDER + 1];
    this.children = new Object[ORDER + 2];
  }

  public Node getChild(int index) {
    return (Node) this.children[index];
  }

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

  @Override
  public int search(String key) {
    int index = 0;
    for (index = 0; index < this.getElementCount(); ++index) {
      int cpr = ((String) this.getElement(index)).toLowerCase().compareTo(key.toLowerCase());
      if (cpr == 0) {
        return index + 1;
      } else if (cpr > 0) {
        return index;
      }
    }
    return index;
  }

  private void insertAt(int index, String key, Node leftChild, Node rightChild) {
    // move space for the new key
    for (int i = this.getElementCount() + 1; i > index; --i) {
      this.setChild(i, this.getChild(i - 1));
    }
    for (int i = this.getElementCount(); i > index; --i) {
      this.setElement(i, this.getElement(i - 1));
    }

    // insert the new key
    this.setElement(index, key);
    this.setChild(index, leftChild);
    this.setChild(index + 1, rightChild);
    this.elementCount += 1;
  }

  @Override
  protected InnerNode split() {
    int midIndex = this.getElementCount() / 2;

    InnerNode newNode = new InnerNode();
    for (int i = midIndex + 1; i < this.getElementCount(); ++i) {
      newNode.setElement(i - midIndex - 1, this.getElement(i));
      this.setElement(i, null);
    }
    for (int i = midIndex + 1; i <= this.getElementCount(); ++i) {
      newNode.setChild(i - midIndex - 1, this.getChild(i));
      newNode.getChild(i - midIndex - 1).setParent(newNode);
      this.setChild(i, null);
    }
    this.setElement(midIndex, null);
    newNode.elementCount = this.getElementCount() - midIndex - 1;
    this.elementCount = midIndex;

    return newNode;
  }

  @Override
  protected Node solveOverflow() {
    Node newNode = this.split();

    if (this.getParent() == null) {
      this.setParent(new InnerNode());
    }
    newNode.setParent(this.getParent());

    // push up a key to parent internal node
    int midIndex = this.getElementCount() / 2;
    String upKey = (String) this.getElement(midIndex);

    InnerNode newInnerNode = (InnerNode) this.getParent();

    return newInnerNode.pushKeyUp(upKey, this, newNode);
  }

  protected Node pushKeyUp(String key, Node leftChild, Node rightNode) {
    // find the target position of the new key
    int index = this.search(key);

    // insert the new key
    this.insertAt(index, key, leftChild, rightNode);

    // check whether current node need to be split
    if (this.isOverflow()) {
      return this.solveOverflow();
    } else {
      return this.getParent() == null ? this : null;
    }
  }

}
