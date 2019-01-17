package io.github.ficcitong.kvstore;

public interface KeysAndValues {

  void accept(String kvPairs);

  String display();

}