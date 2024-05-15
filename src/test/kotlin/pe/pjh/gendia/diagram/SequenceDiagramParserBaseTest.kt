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

	actor  User
	participant testData.sequence.SimpleBase
	participant testData.sequence.TestFun
	participant testData.test.SimpleClzz

	User->>testData.sequence.SimpleBase:Call/testRun

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

	actor  User
	participant testData.sequence.SimpleBase

	User->>testData.sequence.SimpleBase:Call/staticMethod
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }

    /**
     * 타 객체 메소스 호출하는 메소드 테스트
     */
    fun testOuterInstanceMethod() {


        val sd = fileAndParamInit(
            "testData.sequence.SimpleBase.outerInstanceMethod",
            "testData/sequence/SimpleBase.java", "testData/sequence/TestFun.java", "testData/test/SimpleClzz.java"
        )

        sd.collection()
        sd.analysis()
        val code = sd.generate()

        logger.info(sd.config.toString())
        logger.info(code)

        TestCase.assertEquals(
            """
	autonumber

	actor  User
	participant testData.sequence.SimpleBase
	participant testData.sequence.TestFun
	participant testData.test.SimpleClzz

	User->>testData.sequence.SimpleBase:Call/outerInstanceMethod
	testData.sequence.SimpleBase->>testData.sequence.TestFun:외부 instance method/testCall
	testData.sequence.SimpleBase->>testData.test.SimpleClzz:타 패키지 instance method/testReturnCall
	testData.test.SimpleClzz-->>testData.sequence.SimpleBase:Integer
	testData.sequence.SimpleBase->>testData.sequence.TestFun:외부 instance return method/testReturnCall
	testData.sequence.TestFun-->>testData.sequence.SimpleBase:Integer
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }

    /**
     * 인라인 변수로 선언된 메소드 주석 테스트
     */
    fun testInlineVariable() {
        val sd = fileAndParamInit(
            "testData.sequence.SimpleBase.testInlineVariable",
            "testData/sequence/SimpleBase.java"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
	autonumber

	actor  User
	participant testData.sequence.SimpleBase

	User->>testData.sequence.SimpleBase:Call/testInlineVariable
	testData.sequence.SimpleBase->>testData.sequence.SimpleBase:단순 호출/getStringFortestInlineVariable
	testData.sequence.SimpleBase->>testData.sequence.SimpleBase:문자열 반환된 값 변수 대입/getStringFortestInlineVariable
	testData.sequence.SimpleBase->>testData.sequence.SimpleBase: 문자열 반환된 값 변수 대입, 서브 재호출/getSubCallForTestInlineVariable
	testData.sequence.SimpleBase->>testData.sequence.SimpleBase: 메소드호출 및 문자열 인라인 변수 선언./getStringFortestInlineVariable
	testData.sequence.SimpleBase->>testData.sequence.SimpleBase: 메소드호출 및 문자열 인라인 변수 선언./getStringFortestInlineVariable
            """.trimIndent(),
            code.trimIndent()
        )
    }
}