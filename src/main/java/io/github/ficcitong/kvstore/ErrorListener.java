package io.github.ficcitong.kvstore;

import java.util.Set;

public interface ErrorListener {

  void onError(String msg);

  void onError(String msg, Exception e);

  void onIncompleteAtomicGroup(Set<String> group, Set<String> missing);

}