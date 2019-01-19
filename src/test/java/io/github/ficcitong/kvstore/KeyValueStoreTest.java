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
  public void testCase1() {
    /**
     * A general test to test one insert.
     */
    kvStoreToTest.accept("1=1");
    String expectedStr = "1=1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase2() {
    /**
     * Empty input test
     */
    kvStoreToTest.accept("");
    String expectedStr = "-1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase3() {
    /**
     * Test the ability to handle multiple accepts into one key value store.
     */
    kvStoreToTest.accept("1=1");
    kvStoreToTest.accept("2=2");
    kvStoreToTest.accept("3=3");
    kvStoreToTest.accept("4=4");
    kvStoreToTest.accept("5=5");
    kvStoreToTest.accept("6=6");
    kvStoreToTest.accept("7=7");
    kvStoreToTest.accept("8=8");
    kvStoreToTest.accept("9=9");
    kvStoreToTest.accept("0=0");

    String expectedStr = "0=0\n1=1\n2=2\n3=3\n4=4\n5=5\n6=6\n7=7\n8=8\n9=9";

    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase4() {
    /**
     * Test the ability to take in one line of csv string.
     */
    kvStoreToTest.accept("1=1,2=2,3=3,4=4,5=5,6=6,7=7,8=8,9=9,0=0");
    String expectedStr = "0=0\n1=1\n2=2\n3=3\n4=4\n5=5\n6=6\n7=7\n8=8\n9=9";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase5() {
    /**
     * Test the ability to take in both numeric values and string values.
     */
    kvStoreToTest.accept("1=1");
    kvStoreToTest.accept("a=a");
    String expectedStr = "1=1\na=a";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase6() {
    /**
     * Test the ability to handle white spaces.
     */
    kvStoreToTest.accept("  1  =  1  ,   a = a ");
    String expectedStr = "1=1\na=a";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase7() {
    /**
     * Case insensitivity test
     */
    kvStoreToTest.accept("  1  =  1  ,   a = a ");
    String expectedStr = "1=1\na=a";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase8() {
    /**
     * Test the ability to handle addition for numeric values.
     */
    kvStoreToTest.accept("1=1,1=2,1=3,1=4,1=5");
    String expectedStr = "1=15";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase9() {
    /**
     * Test the ability to handle negative numbers.
     */
    kvStoreToTest.accept("a=1,a=-1000");
    String expectedStr = "a=-999";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase10() {
    /**
     * Test the ability to replace negative number with string
     */
    kvStoreToTest.accept("a=-999,a=hahaha");
    String expectedStr = "a=hahaha";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase11() {
    /**
     * Test the ability to handle replacement for number and string.
     */
    kvStoreToTest.accept("1=1,1=a,a=b,a=0");
    String expectedStr = "1=a\na=0";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase12() {
    /**
     * Test that same result will be print out no matter what the order of input.
     */

    kvStoreToTest.accept("1=1");
    kvStoreToTest.accept("2=2");
    kvStoreToTest.accept("3=3");
    kvStoreToTest.accept("4=4");
    kvStoreToTest.accept("5=5");
    kvStoreToTest.accept("6=6");
    kvStoreToTest.accept("7=7");
    kvStoreToTest.accept("8=8");
    kvStoreToTest.accept("9=9");
    kvStoreToTest.accept("0=0");

    KeyValueStore kvStoreToTest1 = new KeyValueStore(new KeyValueStoreErrorListener());
    kvStoreToTest1.accept("7=7");
    kvStoreToTest1.accept("3=3");
    kvStoreToTest1.accept("9=9");
    kvStoreToTest1.accept("5=5");
    kvStoreToTest1.accept("4=4");
    kvStoreToTest1.accept("6=6");
    kvStoreToTest1.accept("2=2");
    kvStoreToTest1.accept("8=8");
    kvStoreToTest1.accept("0=0");
    kvStoreToTest1.accept("1=1");

    String expectedStr = "0=0\n1=1\n2=2\n3=3\n4=4\n5=5\n6=6\n7=7\n8=8\n9=9";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
    assertTrue(expectedStr.equals(kvStoreToTest1.display()));
    assertTrue(kvStoreToTest.display().equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase13() {
    /**
     * Test that same result will be print out no matter what the order of input.
     */

    kvStoreToTest.accept("1=1,2=2,3=3,4=4,5=5,6=6,7=7,8=8,9=9,0=0");

    KeyValueStore kvStoreToTest1 = new KeyValueStore(new KeyValueStoreErrorListener());
    kvStoreToTest1.accept("0=0,7=7,1=1,3=3,9=9,4=4,2=2,5=5,6=6,8=8");

    String expectedStr = "0=0\n1=1\n2=2\n3=3\n4=4\n5=5\n6=6\n7=7\n8=8\n9=9";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
    assertTrue(expectedStr.equals(kvStoreToTest1.display()));
    assertTrue(kvStoreToTest.display().equals(kvStoreToTest.display()));
  }

  // Tests with errors in input strings
  @Test
  public void testCase14() {
    /**
     * If there is error, the whole string should not be inserted
     */
    kvStoreToTest.accept("1=5,=3");

    String expectedStr = "-1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase15() {
    /**
     * Only the errored string will not be inserted.
     */

    kvStoreToTest.accept("1=1");
    kvStoreToTest.accept("2=2");
    // Errored input
    kvStoreToTest.accept("3=");
    kvStoreToTest.accept("4=4");
    kvStoreToTest.accept("5=5");

    String expectedStr = "1=1\n2=2\n4=4\n5=5";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  // Test atomic groups
  @Test
  public void testCase16() {
    /**
     * Test complete atomic groups
     */
    kvStoreToTest.accept("1=1,441=1,442=1,500=1");
    String expectedStr = "1=1\n441=1\n442=1\n500=1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase17() {
    /**
     * Test multiple complete atomic groups with value addition or replacement
     */
    kvStoreToTest.accept("1=1,441=1,442=a,500=1,441=1,442=b,500=c");
    String expectedStr = "1=1\n441=2\n442=b\n500=c";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase18() {
    /**
     * Test one incomplete atomic group
     */
    kvStoreToTest.accept("1=1,441=1,442=a");
    String expectedStr = "1=1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

  @Test
  public void testCase19() {
    /**
     * The second atomic group is incomplete
     */
    kvStoreToTest.accept("1=1,441=1,442=a,500=1,500=1,441=b");
    String expectedStr = "1=1\n441=1\n442=a\n500=1";
    assertTrue(expectedStr.equals(kvStoreToTest.display()));
  }

}
