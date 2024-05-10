package testData.sequence;


import testData.test.SimpleClzz;

public class SimpleBase {

    private final TestFun testFun;
    private SimpleClzz simpleClzz;

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
    public void staticMethod() {

        //+ 외부 static method 무시 처리 테스트
//        TestFun.returnStr();

        //+콘솔 출력3
        staticTest();

        //+내부 static method 호출
        staticMethodCall();
    }

    /**
     * 외부 method 호출
     */
    public void outerInstanceMethod() {

        //+외부 instance method
        this.testFun.testCall();

        //+타 패키지 instance method
        this.simpleClzz.testReturnCall();

        //+외부 instance return method
        Integer i = this.testFun.testReturnCall();
        System.out.println(i);
    }

    /**
     * 인라인 테스트 케이스 테스트.
     */
    public void testInlineVariable() {

        int a = 0, b = 0, c = 0;

        //+단순 호출
        getStringFortestInlineVariable();

        //+문자열 반환된 값 변수 대입
        String test1 = getStringFortestInlineVariable();

        //+ 문자열 반환된 값 변수 대입, 서브 재호출
        String test2 = getSubCallForTestInlineVariable();
        System.out.println(test1);

        //+ 2열로 인라인 호출
        String test2_1 = getStringFortestInlineVariable(),
                test2_2 = getStringFortestInlineVariable();
    }


    public static void staticTest() {
        System.out.println("simple:key with spaces1");
    }

    public static void staticMethodCall() {
        //+클래스 내부 static Method Call
        staticMethodInnerCall();
    }

    public static void staticMethodInnerCall() {

        System.out.println("simple:key with spaces1");
    }

    public String getSubCallForTestInlineVariable() {
        //+Callee SubCall
        return getStringFortestInlineVariable();
    }

    public String getStringFortestInlineVariable() {
        //+Callee String
        return "string";
    }
}
