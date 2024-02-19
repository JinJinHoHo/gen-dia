package pe.pjh.gendia.diagram

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase

public class MyTest : BasePlatformTestCase() {
    fun testSample() {

        // Prepare the test data
        myFixture.configureByText("MyTestClass.kt", "class MyTestClass {}")

        // Do something with the IDE (like invoking an intention action)

        // Check that the result is what you expected
        myFixture.checkResult("class MyTestClass { fun newFunction(){} }")
//
//        // Create a new project for test
//        configureByText("MyTestClass.kt",
//            """
//                fun main() {
//                    println("Hello, world!")
//                }
//                """.trimIndent()
//        )
//
//        // Add the source root
//        PsiTestUtil.addSourceRoot(module, file.virtualFile.parent)
//
//        val javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(psiManager.getProject())
//
//        javaPsiFacade.findClass("MyTestClass", GlobalSearchScope.allScope(project))
    }
}