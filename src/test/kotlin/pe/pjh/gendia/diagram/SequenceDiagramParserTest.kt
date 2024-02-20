package pe.pjh.gendia.diagram

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.usageView.UsageInfo
import junit.framework.TestCase
import org.junit.Assert


//
class SequenceDiagramParserTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testRenameElementAtCaret() {
        myFixture.configureByFiles("RenameTestData.java", "RenameTestData.simple")
        myFixture.renameElementAtCaret("websiteUrl")
        myFixture.checkResultByFile("RenameTestData.simple", "RenameTestDataAfter.simple", false)
    }


    fun testFindUsages() {

        val files: Array<PsiFile> = myFixture.configureByFiles("FindUsagesTestData.java")
        files.forEach { println(it.getName()) }

        val myClass = JavaPsiFacade.getInstance(project)
            .findClass("Test", module.getModuleWithDependenciesAndLibrariesScope(false))

        val myMethod: PsiElement = myClass!!.findMethodsByName("main", false)[0]

        assertNotNull(myMethod)
    }



    fun testCollection() {

        val files: Array<PsiFile> = myFixture.configureByFiles("FindUsagesTestData.java")

        val sd :SequenceDiagramParser = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("Test.main")
            )
        )

        sd.collection();
        TestCase.assertEquals(1,sd.startPointPsiMethods.size)

        sd.analysis();



//        files.forEach { println(it.getName()) }
//
//        val myClass = JavaPsiFacade.getInstance(project)
//            .findClass("Test", module.getModuleWithDependenciesAndLibrariesScope(false))
//
//        val myMethod: PsiElement = myClass!!.findMethodsByName("main", false)[0]
//
//        assertNotNull(myMethod)
    }

}