package io.github.ficcitong.kvstore;

import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;



public class KeyValueStoreTest {

  private KeyValueStore kvStoreToTest;

  @Before
  public void setUp() throws Exception {
    kvStoreToTest = new KeyValueStore(new KeyValueStoreErrorListener());
  }

  @Test
  public void testKeyValueStore() {
    String expectedStr = "1=3\n2=TEST";
    kvStoreToTest.accept("1=1,    1=2, 2=0, 2 = TEST");
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testKeyValueStore1() {
    String expectedStr = "1=gg\n441=1\n442=2\n500=3";
    kvStoreToTest.accept("1=gg,441=1, 442=2, 500=3");
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

}
