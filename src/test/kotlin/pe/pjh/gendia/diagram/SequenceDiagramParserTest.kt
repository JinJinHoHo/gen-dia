package pe.pjh.gendia.diagram

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase
import org.junit.Assert

//
class SequenceDiagramParserTest : LightJavaCodeInsightFixtureTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }


    protected fun doTest(testName: String, hint: String?) {
        myFixture.configureByFile("$testName.java")
        val action = myFixture.findSingleIntention(hint!!)
        Assert.assertNotNull(action)
        myFixture.launchAction(action)
        myFixture.checkResultByFile("$testName.after.java")
    }

    fun testCollection() {
        SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("pe.pjh.ws.application.service.AppInitializer.startUp")
            )

        )
        doTest("before.template", "SDK: Convert ternary operator to if statement")
    }

    fun testRenameElementAtCaret() {
        myFixture.configureByFiles("RenameTestData.java", "RenameTestData.simple")
        myFixture.renameElementAtCaret("websiteUrl")
        myFixture.checkResultByFile("RenameTestData.simple", "RenameTestDataAfter.simple", false)
    }


    fun testFindUsages() {
        val usageInfos = myFixture.testFindUsages("FindUsagesTestData.simple", "FindUsagesTestData.java")
        assertEquals(1, usageInfos.size)
    }
}