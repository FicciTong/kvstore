/**
 * Using google's CheckStyle formatter.
 */

package io.github.ficcitong.kvstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.inject.Inject;

/**
 * My implementation of provided KeysAndValues interface.
 */
public class KeyValueStore implements KeysAndValues {
  private final KeyValueStoreErrorListener errorListener;
  private Node root;

  // Final atomic group set
  public static final String[] ATOMIC_GROUP_VALUES = new String[] {"441", "442", "500"};
  public static final Set<String> ATOMIC_GROUP = new HashSet<>(Arrays.asList(ATOMIC_GROUP_VALUES));

  // Dependency injection of the error listener, instead of creating a global object.
  @Inject
  public KeyValueStore(KeyValueStoreErrorListener errorlListener) {
    this.root = new LeafNode();
    this.errorListener = errorlListener;
  }

  /**
   * Accepts key value pairs to be inserted into the key value store data structure.
   * 
   * @param kvPairs a csv string containing one or more key value pairs in the form of 'a=a, 1=12',
   *                etc.
   */
  @SuppressWarnings("unchecked")
  public void accept(String kvPairs) {
    // If the input is empty, handle empty error.
    if ("".equals(kvPairs.trim())) {
      errorListener.onError("Empty String");
      return;
    }

    try {
      ArrayList<KeyValuePair> insertList = new ArrayList<KeyValuePair>();

      LinkedList<KeyValuePair>[] atomicListArray = new LinkedList[3];
      for (int i = 0; i < atomicListArray.length; i += 1) {
        atomicListArray[i] = new LinkedList<KeyValuePair>();
      }

      String[] kvPairsList = kvPairs.trim().split(",");
      for (String kvPair : kvPairsList) {
        try {
          String key = kvPair.split("=")[0].trim();
          String value = kvPair.split("=")[1].trim();

          if ("".equals(key) || "".equals(value)) {
            errorListener.onError("Error with specific input pair: " + kvPair);
            return;
          }

          // Inserting members of atomic group into corresponding list.
          switch (key) {
            case "441":
              atomicListArray[0].add(new KeyValuePair(key, value));
              break;
            case "442":
              atomicListArray[1].add(new KeyValuePair(key, value));
              break;
            case "500":
              atomicListArray[2].add(new KeyValuePair(key, value));
              break;
            default:
              insertList.add(new KeyValuePair(key, value));
              break;
          }
        } catch (Exception e) {
          errorListener.onError("Error with specific input pair: " + kvPair, e);
          return;
        }
      }

      // Get complete atomic group count
      int atomicCount = -1;
      for (int i = 0; i < 3; i++) {
        if (atomicCount == -1) {
          atomicCount = atomicListArray[i].size();
        } else {
          atomicCount = Math.min(atomicCount, atomicListArray[i].size());
        }
      }

      // Pop complete groups and insert into insertList
      while (atomicCount != 0) {
        insertList.add(atomicListArray[0].remove());
        insertList.add(atomicListArray[1].remove());
        insertList.add(atomicListArray[2].remove());
        atomicCount -= 1;
      }

      // Check if there are incomplete atomic groups
      if (atomicListArray[0].size() + atomicListArray[1].size() + atomicListArray[2].size() > 0) {
        // Get the missing atomic group set
        HashSet<String> missingSet = new HashSet<String>();
        for (int i = 0; i < 3; i++) {
          if (atomicListArray[i].size() == 0) {
            switch (i) {
              case 0:
                missingSet.add("441");
                break;
              case 1:
                missingSet.add("442");
                break;
              case 2:
                missingSet.add("500");
                break;
              default:
                break;
            }
          }
        }

        // Report incomplete atomic group only if missing set is not empty
        if (missingSet.size() > 0) {
          errorListener.onIncompleteAtomicGroup(ATOMIC_GROUP, missingSet);
        }
      }


      // Finally inserting the list
      for (KeyValuePair kvPair : insertList) {
        insert(kvPair);
      }
    } catch (Exception e) {
      errorListener.onError("Error when accepting key value pair list", e);
    }
  }

  /**
   * A method to display all the stored key value pairs in a multi-line string.
   * 
   * @return a multi-line string containing all key value pairs stored in the data structure.
   */
  public String display() {
    // Report error if the tree is empty. Could've provide a isEmpty() method instead.
    if (this.root.getNodeType() == NodeType.LeafNode && this.root.getElementCount() == 0) {
      errorListener.onError("The Key Value Store is empty");
      return "-1";
    }
    try {
      Node node = this.root;

      // If there is only one leaf, the print the leaf.
      if (node.getNodeType() == NodeType.LeafNode) {
        return ((LeafNode) node).printToString();
      }

      // Find the left most leaf.
      while (node.getNodeType() == NodeType.InnerNode) {
        node = ((InnerNode) node).getChild(0);
      }
      LeafNode leaf = (LeafNode) node;

      ArrayList<String> strArr = new ArrayList<String>();

      // Get all string representation of leafs linked by the sibling link.
      while (leaf != null) {
        strArr.add(leaf.printToString());
        leaf = leaf.getRightSibling();
      }

      return String.join("\n", strArr);
    } catch (Exception e) {
      errorListener.onError("Error displaying the key value store", e);
      return "-1";
    }
  }

  /**
   * Inserts a single key value pair.
   * 
   * @param kvPair a single KeyValuePair object.
   */
  private void insert(KeyValuePair kvPair) {
    try {
      Node node = this.root;

      // Find the leaf node to be inserted the new key value pair.
      while (node.getNodeType() == NodeType.InnerNode) {
        node = ((InnerNode) node).getChild(node.search(kvPair.getKey()));
      }
      LeafNode leaf = (LeafNode) node;
      leaf.insertKeyValuePair(kvPair);

      // Solve leaf overflow if needed.
      if (leaf.isOverflow()) {
        Node newNode = leaf.solveOverflow();

        // If current node have no parent, this is node.
        if (newNode != null) {
          this.root = newNode;
        }
      }
    } catch (Exception e) {
      errorListener.onError("Error when inserting key value pair - " + kvPair.printToString(), e);
      return;
    }

  }

}
