package io.github.ficcitong.kvstore;

class KeyValuePair {
  private String key;
  private String value;

  public KeyValuePair(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  protected String printToString() {
    return this.key + "=" + this.value;
  }

}
