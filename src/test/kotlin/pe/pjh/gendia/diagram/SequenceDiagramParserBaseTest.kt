package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import junit.framework.TestCase


//
class SequenceDiagramParserBaseTest : SeqBasePlatform() {


    fun testCollection() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleBase.testRun"
        )

        sd.collection()
        sd.startPointPsiMethods.forEach {
            val clz = it.parent

            val packageName: String = (clz.containingFile as PsiJavaFile).packageName
            println(clz)
            println(packageName)
            println(it.toString())
        }
        TestCase.assertEquals(1, sd.startPointPsiMethods.size)

    }

    fun testAnalysis() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleBase.testRun"
        )

        sd.collection()
        sd.analysis()
    }

    fun testGenerate1() {


        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleBase.testRun"
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


    fun testStaticMethod() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleBase.staticMethod"
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
	participant testData.sequence.SimpleBase

	User->>testData.sequence.SimpleBase:staticMethod
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }

    fun testOuterInstanceMethod() {


        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleBase.outerInstanceMethod"
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

    fun testGenerate4() {
        val sd = fileAndParamInit(
            "testData/sequence/SimpleReturn.java",
            "testData.sequence.SimpleReturn.testRun"
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