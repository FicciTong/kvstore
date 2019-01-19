package io.github.ficcitong.kvstore;

/**
 * App.
 */
public class App {
  /**
   * Main method.
   * 
   * @param args args list
   */
  public static void main(String[] args) {
    KeysAndValues kvStoreToTest = new KeyValueStore(new KeyValueStoreErrorListener());
    kvStoreToTest.accept("1=0,1=-3");
    String displayText = kvStoreToTest.display();
    System.out.println(displayText);
  }
}
