import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.testFramework.fixtures.IdeaTestExecutionPolicy;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import java.util.List;

public class MyPluginTest extends BasePlatformTestCase {
//https://youtrack.jetbrains.com/issue/TW-73822/Intellij-Inspection-fails-with-JDK17-with-IllegalAccessError-s

    public void testMyPlugin() {
        myFixture.configureByFiles("CompleteTestData.java", "DefaultTestData.simple");
        myFixture.complete(CompletionType.BASIC);
        List<String> lookupElementStrings = myFixture.getLookupElementStrings();
        assertNotNull(lookupElementStrings);
        assertSameElements(lookupElementStrings, "key with spaces", "language", "message", "tab", "website");
    }

    @Override
    protected String getTestDataPath() {
        System.out.println("TestDataPath:" + super.getTestDataPath());
        System.out.println("TestDataPath:" + IdeaTestExecutionPolicy.getHomePathWithPolicy() + "/src/test/testData");
        // 이 메소드는 테스트 데이터 파일의 경로를 리턴해야 합니다.
        return "/Users/pjhplayer/Projects/schl/gen-dia/src/test/resources/testData";
//        return "src/test/testData";
    }
}