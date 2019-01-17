package io.github.ficcitong.kvstore;

public class KeyValueStore implements KeysAndValues {
  private Node root;

  public KeyValueStore() {
    this.root = new LeafNode();
  }

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

  public void accept(String kvPairs) {
    String key = kvPairs.split("=")[0].trim();
    String value = kvPairs.split("=")[1].trim();
    System.out.println(key + ":" + value);

    KeyValuePair kvp = new KeyValuePair(key, value);

    this.insert(kvp);
  }

  public String display() {
    return "" + this.root.getElementCount();
  }
}
