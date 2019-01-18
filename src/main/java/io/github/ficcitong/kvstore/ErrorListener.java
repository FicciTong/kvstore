package io.github.ficcitong.kvstore;

import java.util.Set;

/**
 * The ErrorListener interface provided by the UBS home test instructions. The Implementation is
 * KeyValueStoreErrorListener class.
 */
public interface ErrorListener {
  void onError(String msg);

  void onError(String msg, Exception e);

  void onIncompleteAtomicGroup(Set<String> group, Set<String> missing);

}
