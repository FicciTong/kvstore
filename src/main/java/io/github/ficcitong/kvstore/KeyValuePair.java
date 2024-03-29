package io.github.ficcitong.kvstore;

/**
 * Key value pair object.
 */
class KeyValuePair {
  private String key;
  private String value;

  // Constructor
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

  /**
   * Print the string representation of the pair.
   * 
   * @return a string representation of the pair, e.g. "1=3"
   */
  protected String printToString() {
    return this.key + "=" + this.value;
  }

}
