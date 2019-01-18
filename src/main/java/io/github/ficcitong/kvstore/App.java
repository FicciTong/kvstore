package io.github.ficcitong.kvstore;

public class App {
  /**
   * @param args.
   */
  public static void main(String[] args) {

    KeysAndValues kv = new KeyValueStore();
    kv.accept("0=0");
    kv.accept("1=1");
    kv.accept("2=2");
    kv.accept("3=3");
    kv.accept("4=4");
    kv.accept("5=5");
    kv.accept("6=6");
    kv.accept("7=7");
    kv.accept("8=8");
    kv.accept("9=9");
    kv.accept("10=10");
    kv.accept("11=11");
    kv.accept("12=12");
    kv.accept("13=13");
    kv.accept("14=14");
    kv.accept("15=15");
    kv.accept("16=16");
    kv.accept("17=17");
    kv.accept("18=18");
    kv.accept("19=19");
    kv.accept("20=20");
    kv.accept("21=21");
    kv.accept("22=22");
    kv.accept("23=23");
    kv.accept("24=24");
    kv.accept("25=25");
    kv.accept("26=26");
    kv.accept("27=27");
    kv.accept("28=28");
    kv.accept("29=29");
    kv.accept("one=two");
    kv.accept("Three=four");
    kv.accept("5=6");
    kv.accept("14=X");
    kv.accept("three=fourty");
    kv.accept("uuuuu=9");
    kv.accept("uuuuu=1");
    String displayText = kv.display();
    System.out.println(displayText);
  }
}
