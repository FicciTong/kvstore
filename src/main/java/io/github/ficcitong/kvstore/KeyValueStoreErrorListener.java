package io.github.ficcitong.kvstore;

import java.util.Set;

/**
 * My implementation of the provided ErrorListener interface.
 */
class KeyValueStoreErrorListener implements ErrorListener {
  /**
   * Report when error.
   * 
   * @param msg the string error message
   */
  @Override
  public void onError(String msg) {
    System.out.println("Error: " + msg);
  }

  /**
   * Report when error.
   * 
   * @param msg the customized error message string
   * @param e   the error it self
   */
  @Override
  public void onError(String msg, Exception e) {
    System.out.println("Error: " + msg);
    System.out.println("Error Details: " + e.toString());
  }

  /**
   * Error handler for atomic group, aka SET("441", "442", "500").
   * 
   * @param group   the atomic group defined in the instruction SET("441", "442", "500")
   * @param missing the missing set of members of the atomic group
   */
  @Override
  public void onIncompleteAtomicGroup(Set<String> group, Set<String> missing) {
    System.out.println("Error: Imcomplete Atomic Group");
    System.out.println("Error Details: Missing " + String.join(", ", missing));
  }

}
