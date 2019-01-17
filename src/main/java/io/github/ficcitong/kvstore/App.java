package io.github.ficcitong.kvstore;

public class App {

  public static void main(String[] args) {

    KeysAndValues kv = new KeyValueStore<String>();
    kv.accept("one=two ");
    kv.accept("Three= four");
    kv.accept("5=6");
    kv.accept("one=X");
    String displayText = kv.display();
    System.out.println(displayText);
  }
}
