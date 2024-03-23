
package testData.sequence;

public class SimpleLoop {
    public String testRun1() {
        System.out.println("simple:key with spaces");

        //+루프 테스트
        for (int i = 0; i < 10; i++) {
            System.out.println("루프 테스트");
        }
        return "테스트";
    }

    public String testRun2() {
        System.out.println("simple:key with spaces");

        //+루프 테스트
        for (int i = 0; i < 10; i++) {
            //+서브 메소드 콜
            newMethod();
        }
        return "테스트";
    }

    public String testRun3() {
        System.out.println("simple:key with spaces");

        for (int i = 0; i < 10; i++) {
            //+서브 메소드 콜
            newMethod();
        }
        return "테스트";
    }

    public void newMethod() {
        System.out.println("This is a new method");
    }
}
