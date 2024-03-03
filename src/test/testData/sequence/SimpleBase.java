
package sequence
public class SimpleBase {

  /**
   * 리턴값 없음.
   */
  public void testRun() {
    System.out.println("simple:key with spaces");
  }


  /**
   * 리턴값 없음.
   */
  public void testRun2() {

    //+콘솔 출력
    System.out.println("simple:key with spaces");

    //+콘솔 출력2
    TestFun.returnStr();

    //+콘솔 출력3
    staticTest();
  }


  public static staticTest(){
    System.out.println("simple:key with spaces1");
  }
}
