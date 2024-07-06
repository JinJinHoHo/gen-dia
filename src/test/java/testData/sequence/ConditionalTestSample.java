package testData.sequence;

public class ConditionalTestSample {

    public void testIf() {
        System.out.println("simple:key with spaces");

        int i = 10;

        //+분기 처리 타입1
        if (i == 10) {
            //+서브 메소드 콜
            newMethod();
        }
    }


    public void testIfElse() {
        System.out.println("simple:key with spaces");

        int i = 10;

        //+분기 처리 타입2
        if (i == 5) {
            //+서브 메소드 콜
            newMethod();
        } else {
            //+프린트
            newMethod();
            System.out.println("ELSE");
        }

    }


    public void testRun3() {
        System.out.println("simple:key with spaces");

        int i = 10;


        //+분기 처리 타입2
        if (i == 5) {
            //+서브 메소드 콜
            newMethod();
        } else if (i == 7) {
            //+서브 메소드 콜
            newMethod();
        } else {
            //+프린트
            System.out.println("ELSE");
        }

        //+서브 메소드 콜
        if (1 == 5) newMethod();
        else System.out.println("ELSE");
    }


    public void testRun4() {
        System.out.println("simple:key with spaces");

        int i = 10;


        switch (i) {
            case 5: //+서브 메소드 콜
                newMethod();
                break;

            case 6: {
                newMethod();
                break;
            }
            case 7: {
                newMethod();
                return;
            }

            default: {
                System.out.println("default");
            }
        }
    }

    public void newMethod() {
        System.out.println("This is a new method");
    }
}
