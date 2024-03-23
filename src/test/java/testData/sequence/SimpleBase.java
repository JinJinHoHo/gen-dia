
package testData.sequence;


public class SimpleBase {

    private TestFun testFun;

    public SimpleBase() {
        this.testFun = new TestFun();
    }

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

        //+콘솔 출력2
        TestFun.returnStr();

        //+콘솔 출력3
        staticTest();
    }

    /**
     * 외부/내부 static method 호출
     */
    public void testRun3() {

        //+외부 static method
        TestFun.returnStr();

        //+내부 static method
        staticTest();

        //+외부 instatce method
        this.testFun.testCall();

        //+외부 instatce return method
        Integer i = this.testFun.testReturnCall();
        System.out.println(i);
    }

    public void testRun4() {

        int a = 0, b = 0, c = 0;

        //+메소드 호출
        getString();

        //+메소드 호출2
        String test1 = getString();

        //+메소드 호출2
        String test2 = getString();
        System.out.println(test1);


        String test2_1 = getString(), test2_2 = getString();
    }


    public static void staticTest() {
        System.out.println("simple:key with spaces1");
    }

    public String getString() {
        return "string";
    }
}
