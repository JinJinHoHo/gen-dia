package testData.sequence;

public class SimpleInBlock {
    public String testRun1() {
        System.out.println("simple:key with spaces");

        for (int i = 0; i < 10; i++) {
            //+서브 메소드 콜
            newMethod();
        }

        int idx = 10;
        if (idx == 10) {
            //+서브 메소드 콜
            newMethod();
        }
        return "테스트";
    }

    public void newMethod() {
        System.out.println("This is a new method");
    }
}
