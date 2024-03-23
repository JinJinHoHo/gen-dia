package testData.sequence;

public class TestFun {
    public static String returnStr() {
        return "returnStr";
    }

    public static void printStr() {
        System.out.println("printStr");
    }

    public void testCall() {
        System.out.println("printStr");
    }

    public Integer testReturnCall() {
        return 1 + 1;
    }
}
