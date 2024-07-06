package testData.sequence;

import java.io.File;
import java.io.IOException;

public class TryCacheConditionalTestSample {
    public void testRun1() throws IOException {

        int i = 10;

        //+예외처리
        try {
            //+파일 지정
            newMethod();

            //+파일 지정
            File file = newMethod();

            file.createNewFile();
        } catch (IOException e) {
            throw e;
        } finally {
            System.out.println("finally");
        }

    }


    public File newMethod() {
        return new File("/test");
    }

}
