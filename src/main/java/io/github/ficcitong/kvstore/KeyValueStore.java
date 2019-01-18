package io.github.ficcitong.kvstore;

import java.awt.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

// import javax.inject.Inject;

public class KeyValueStore implements KeysAndValues {
  private Node root;

  public KeyValueStore() {
    this.root = new LeafNode();
  }

  /**
   * 
   * @param kvPair.
   */
  public void insert(KeyValuePair kvPair) {
    Node node = this.root;
    while (node.getNodeType() == NodeType.InnerNode) {
      node = ((InnerNode) node).getChild(node.search(kvPair.getKey()));
    }
    LeafNode leaf = (LeafNode) node;
    leaf.insertKeyValuePair(kvPair);

    if (leaf.isOverflow()) {
      Node n = leaf.solveOverflow();
      if (n != null) {
        this.root = n;
      }
    }
  }

  /**
   * @param kvPairs.
   */
  public void accept(String kvPairs) {
    String key = kvPairs.split("=")[0].trim();
    String value = kvPairs.split("=")[1].trim();
    KeyValuePair kvp = new KeyValuePair(key, value);
    this.insert(kvp);
  }

  /**
   * @return string.
   */
  public String display() {
    Node node = this.root;
    if (node.getNodeType() == NodeType.LeafNode) {
      return ((LeafNode) node).printToString();
    }

    while (node.getNodeType() == NodeType.InnerNode) {
      node = ((InnerNode) node).getChild(0);
    }
    LeafNode leaf = (LeafNode) node;

    ArrayList<String> strArr = new ArrayList<String>();

    while (leaf != null) {
      strArr.add(leaf.printToString());
      leaf = leaf.getRightSibling();
    }

    return String.join("\n", strArr);
  }
}
