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
    KeysAndValues kv = new KeyValueStore(new KeyValueStoreErrorListener());
    kv.accept("1=gg, 441=1, 442=2, 500=3, 500=8");
    kv.accept("one=");
    kv.accept("Three=four");
    kv.accept("5=6");
    kv.accept("14=X");
    String displayText = kv.display();
    System.out.println(displayText);

  }
}
