package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Test
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
class SequenceDiagramParserBaseTest : SeqBasePlatform() {


    fun testCollection() {

        val sd = fileAndParamInit(
            "sequence/SimpleBase.java",
            "sequence.SimpleBase.testRun"
        )

        sd.collection();
        sd.startPointPsiMethods.forEach {
            val clz = it.parent

            val packageName: String = (clz.getContainingFile() as PsiJavaFile).getPackageName()
            println(clz)
            println(packageName)
            println(it.toString())
        }
        TestCase.assertEquals(1, sd.startPointPsiMethods.size)

    }

    fun testAnalysis() {

        val sd = fileAndParamInit(
            "sequence/SimpleBase.java",
            "sequence.SimpleBase.testRun"
        )

        sd.collection()
        sd.analysis()
    }

    fun testGenerate1() {


        val sd = fileAndParamInit(
            "sequence/SimpleBase.java",
            "sequence.SimpleBase.testRun"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
            autonumber
            actor User
            participant User
            participant sequence.SimpleBase
            User->>sequence.SimpleBase:testRun
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }



    fun testGenerate2() {


        val sd = fileAndParamInit(
            "sequence/SimpleBase.java",
            "sequence.SimpleBase.testRun2"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
            autonumber
        
            actor User
        
            participant User
            participant sequence.SimpleBase
        
            User->>sequence.SimpleBase:testRun2
            sequence.SimpleBase->>sequence.SimpleBase:System.out.println
            sequence.SimpleBase->>sequence.SimpleBase:TestFun.returnStr
            sequence.SimpleBase->>sequence.SimpleBase:staticTest
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }

    fun testGenerate3() {
        val sd = fileAndParamInit(
            "sequence/SimpleReturn.java",
            "sequence.SimpleReturn.testRun"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
            autonumber
        
            actor User
        
            participant User
            participant sequence.SimpleReturn
        
            User->>sequence.SimpleReturn:testRun
            sequence.SimpleReturn-->>User:String
            """.trimIndent(),
            code.trimIndent()
        )
    }

}