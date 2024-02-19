package pe.pjh.gendia.diagram

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.impl.JavaPsiFacadeImpl
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.testFramework.PsiTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase

//
//class SequenceDiagramParserTest : FunSpec({
//
//    val testProject:TestProject?
//
//    test("collection") {
////        val mockClassDiagramParser = Mockito.mock(ClassDiagramParser::class.java)
////        val mockPsiClass = Mockito.mock(PsiClass::class.java)
////        val mockProject = Mockito.mock(Project::class.java)
////        val mockPsiFacade = Mockito.mock(JavaPsiFacadeImpl::class.java)
////
////        val foundClass = mockPsiFacade.findClass("ClassDiagramParser", GlobalSearchScope.allScope(mockProject))
////        println(foundClass)
//
//    }
//}) {
//    @BeforeEach
//    fun setUp() {
//
//
//        TestProject();
//    }
//
//    class TestProject : BasePlatformTestCase() {
//        fun test(){
//
//            // Create a new project for test
//            myFixture.configureByText(
//                "MyTestClass.java",
//                "public class MyTestClass {\n" +
//                        "    public static void main(String[] args) {\n" +
//                        "        System.out.println(\"Hello, world!\");\n" +
//                        "    }\n" +
//                        "}"
//            )
//
//
//            // Add the source root
//            PsiTestUtil.addSourceRoot(module, myFixture.file.virtualFile.parent)
//
//        }
//    }
//}
