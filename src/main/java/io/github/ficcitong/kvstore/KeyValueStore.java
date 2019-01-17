package io.github.ficcitong.kvstore;

public class KeyValueStore<E extends Comparable<E>> implements KeysAndValues {
  private Node<E> root;

  public KeyValueStore() {
    this.root = new LeafNode<E>();
  }

  public void insert(KeyValuePair kvPair) {
    Node<E> node = this.root;
    while (node.getNodeType() == NodeType.InnerNode) {
      node = ((InnerNode<E>) node).getChild(node.search(kvPair.getKey()));
    }
    LeafNode<E> leaf = (LeafNode<E>) node;
    leaf.insertKeyValuePair(kvPair);

    if (leaf.isOverflow()) {
      Node<E> n = leaf.solveOverflow();
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
