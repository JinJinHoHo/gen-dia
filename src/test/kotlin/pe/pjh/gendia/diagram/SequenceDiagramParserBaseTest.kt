package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import junit.framework.TestCase
import org.slf4j.LoggerFactory


//
class SequenceDiagramParserBaseTest : SeqBasePlatform() {

    companion object {
        private val logger = LoggerFactory.getLogger(SequenceDiagramParserBaseTest::class.java)
    }

    fun testCollection() {

        val sd = fileAndParamInit(
            "testData.sequence.SimpleBase.testRun",
            "testData/sequence/SimpleBase.java"

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
            "testData.sequence.SimpleBase.testRun",
            "testData/sequence/SimpleBase.java"
        )

        sd.collection()
        sd.analysis()
    }

    fun testGenerate1() {


        val sd = fileAndParamInit(
            "testData.sequence.SimpleBase.testRun",
            "testData/sequence/SimpleBase.java"
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
            "testData.sequence.SimpleBase.staticMethod",
            "testData/sequence/SimpleBase.java"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        logger.info(code)
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
            "testData.sequence.SimpleBase.outerInstanceMethod",
            "testData/sequence/SimpleBase.java","testData/sequence/TestFun.java","testData/test/SimpleClzz.java"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        logger.info(code)
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
            "testData.sequence.SimpleReturn.testRun",
            "testData/sequence/SimpleReturn.java"
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