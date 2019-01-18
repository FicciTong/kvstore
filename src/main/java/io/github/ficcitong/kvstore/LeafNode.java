package io.github.ficcitong.kvstore;

import java.util.ArrayList;

class LeafNode extends Node {

  // Pointer to the right sibling of the current leaf node
  private LeafNode rightSibling;

  // Constructor
  public LeafNode() {
    this.elements = new KeyValuePair[ORDER + 1];
    this.rightSibling = null;
  }

  @Override
  public NodeType getNodeType() {
    return NodeType.LeafNode;
  }

  /**
   * Find the key in the leaf node. Return -1 if not exist.
   * 
   * @return the index of the key, or -1 if noe exist
   */
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

  /**
   * Insert the key value pair to the current leaf node.
   * 
   * @param kvpair the key value pair to be inserted into the leaf node.
   */
  public void insertKeyValuePair(KeyValuePair kvpair) {
    int index = 0;
    // Find the index to be inserted.
    while (index < this.getElementCount() && ((KeyValuePair) this.getElement(index)).getKey()
        .toLowerCase().compareTo(kvpair.getKey().toLowerCase()) < 0) {
      ++index;
    }

    // Check whether the key exists in the node.
    if (this.getElement(index) != null && ((KeyValuePair) this.getElement(index)).getKey()
        .toLowerCase().compareTo(kvpair.getKey().toLowerCase()) == 0) {
      KeyValuePair curKvPair = ((KeyValuePair) this.getElement(index));
      // If exists, add the value if old and new values are all numeric, otherwise replace.
      if (isNumeric(kvpair.getValue()) && isNumeric(curKvPair.getValue())) {
        int newValue = Integer.parseInt(curKvPair.getValue()) + Integer.parseInt(kvpair.getValue());
        curKvPair.setValue("" + newValue);
      } else {
        this.setElement(index, kvpair);
      }
    } else {
      // Insert the key value pair if not exist.
      // Move space for the new key
      for (int i = this.getElementCount() - 1; i >= index; --i) {
        this.setElement(i + 1, this.getElement(i));
      }

      // Insert new key value pair
      this.setElement(index, kvpair);
      ++this.elementCount;
    }

  }

  /**
   * Helper function to determine whether string is numeric or not.
   * 
   * @param str the string in question
   * @return true if is numeric, false if not.
   */
  private static boolean isNumeric(String str) {
    try {
      Integer.parseInt(str);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * Splits the current leaf node and return the new right node.
   * 
   * @return the new node after split.
   */
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

  /**
   * Solve the overflow of a leaf node.
   * 
   * @return the node returned by the pushKeyUp method.
   */
  @Override
  protected Node solveOverflow() {
    LeafNode newNode = this.split();

    // If parent is null, instantiate new inner node as parent.
    if (this.getParent() == null) {
      this.setParent(new InnerNode());
    }
    newNode.setParent(this.getParent());

    // Maintain links of sibling nodes.
    newNode.setRightSibling(this.rightSibling);
    this.setRightSibling(newNode);

    // Push the middle key up to parent inner node.
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

  /**
   * Print the node to a multi-line string representation.
   * 
   * @return the string reprsenting the elements in the current leaf node. E.g, "1=3\n2=yes".
   */
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
