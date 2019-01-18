package io.github.ficcitong.kvstore;

import java.util.ArrayList;

class LeafNode extends Node {

  private LeafNode rightSibling;

  public LeafNode() {
    this.elements = new KeyValuePair[ORDER + 1];
    this.rightSibling = null;
  }

  @Override
  public NodeType getNodeType() {
    return NodeType.LeafNode;
  }

  @Override
  public int search(String key) {
    for (int i = 0; i < this.getElementCount(); ++i) {
      int cpr =
          ((KeyValuePair) this.getElement(i)).getKey().toLowerCase().compareTo(key.toLowerCase());
      if (cpr == 0) {
        return i;
      } else if (cpr > 0) {
        return -1;
      }
    }
    return -1;
  }

  public void insertKeyValuePair(KeyValuePair kvpair) {
    int index = 0;
    while (index < this.getElementCount() && ((KeyValuePair) this.getElement(index)).getKey()
        .toLowerCase().compareTo(kvpair.getKey().toLowerCase()) < 0) {
      ++index;
    }

    if (this.getElement(index) != null && ((KeyValuePair) this.getElement(index)).getKey()
        .toLowerCase().compareTo(kvpair.getKey().toLowerCase()) == 0) {
      // add for integers or replace for integers
      KeyValuePair curKvPair = ((KeyValuePair) this.getElement(index));
      if (isNumeric(kvpair.getValue()) && isNumeric(curKvPair.getValue())) {
        int newValue = Integer.parseInt(curKvPair.getValue()) + Integer.parseInt(kvpair.getValue());
        curKvPair.setValue("" + newValue);
      } else {
        this.setElement(index, kvpair);
      }
    } else {
      // move space for the new key
      for (int i = this.getElementCount() - 1; i >= index; --i) {
        this.setElement(i + 1, this.getElement(i));
      }

      // insert new key and value
      this.setElement(index, kvpair);
      ++this.elementCount;
    }

  }

  private static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  @Override
  protected LeafNode split() {
    int midIndex = this.getElementCount() / 2;

    LeafNode newNode = new LeafNode();
    for (int i = midIndex; i < this.getElementCount(); ++i) {
      newNode.setElement(i - midIndex, this.getElement(i));
      this.setElement(i, null);
    }
    newNode.elementCount = this.getElementCount() - midIndex;
    this.elementCount = midIndex;

    return newNode;
  }

  @Override
  protected Node solveOverflow() {
    LeafNode newNode = this.split();

    if (this.getParent() == null) {
      this.setParent(new InnerNode());
    }
    newNode.setParent(this.getParent());

    // maintain links of sibling nodes
    newNode.setRightSibling(this.rightSibling);
    this.setRightSibling(newNode);

    // push up a key to parent internal node
    int midIndex = this.getElementCount() / 2;
    String upKey = ((KeyValuePair) this.getElement(midIndex)).getKey();

    InnerNode newInnerNode = (InnerNode) this.getParent();
    return newInnerNode.pushKeyUp(upKey, this, newNode);
  }

  public LeafNode getRightSibling() {
    return this.rightSibling;
  }

  public void setRightSibling(LeafNode rightSibling) {
    this.rightSibling = rightSibling;
  }

  protected String printToString() {

    ArrayList<String> strArr = new ArrayList<String>();
    int index = 0;

    while (this.elements[index] != null) {
      strArr.add(((KeyValuePair) this.elements[index]).printToString());
      index += 1;
    }

    return String.join("\n", strArr);
  }

}
