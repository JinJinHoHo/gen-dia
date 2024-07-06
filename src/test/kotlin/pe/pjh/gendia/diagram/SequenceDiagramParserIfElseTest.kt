package pe.pjh.gendia.diagram

import junit.framework.TestCase
import pe.pjh.gendia.diagram.sequence.ParticipantLabelType
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig


//
class SequenceDiagramParserIfElseTest : SeqBasePlatform() {


    /**
     * IfElse 구문 테스트 타입1
     */
    fun testIf() {


        val sd = fileAndParamInit(
            SequenceDiagramConfig(
                UMLType.Mermaid,
                DiagramType.SequenceDiagram,
                "testData.sequence.ConditionalTestSample.testIf",
                web = true,
                participantLabelType = ParticipantLabelType.CLASS_NAME
            ),
            "testData/sequence/ConditionalTestSample.java"
        )

        sd.collection();
        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected = """
	autonumber

	actor  User
	participant testData.sequence.ConditionalTestSample as ConditionalTestSample

	User->>testData.sequence.ConditionalTestSample:Request/testRun1
	Note right of testData.sequence.ConditionalTestSample: 분기 처리 타입1
	alt i == 10
		testData.sequence.ConditionalTestSample->>testData.sequence.ConditionalTestSample:서브 메소드 콜/newMethod
	end
	testData.sequence.ConditionalTestSample-->>User:Response
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }

    /**
     * IfElse 구문 테스트 타입2
     */
    fun testIfElse() {


        val sd = fileAndParamInit(
            SequenceDiagramConfig(
                UMLType.Mermaid,
                DiagramType.SequenceDiagram,
                "testData.sequence.ConditionalTestSample.testIfElse",
                web = false,
                participantLabelType = ParticipantLabelType.CLASS_NAME
            ),

            "testData/sequence/ConditionalTestSample.java"
        )
        sd.collection();
        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected = """
            autonumber
        
            actor User
        
            participant User
            participant sequence.ConditionalTestSample
        
            User->>sequence.ConditionalTestSample:testRun2
            Note right of sequence.ConditionalTestSample: 분기 처리 타입2
            alt i == 5
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:서브 메소드 콜:newMethod
            else else
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:System.out.println
            end
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }

    fun testGenerateByIfElse3() {


        val sd = fileAndParamInit(
            "testData/sequence/ConditionalTestSample.java",
            "testData.sequence.ConditionalTestSample.testRun3"
        )
        sd.collection();
        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected = """
            autonumber
        
            actor User
        
            participant User
            participant sequence.ConditionalTestSample
        
            User->>sequence.ConditionalTestSample:testRun3
            Note right of sequence.ConditionalTestSample: 분기 처리 타입2
            alt i == 5
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:서브 메소드 콜:newMethod
            else i == 7
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:서브 메소드 콜:newMethod
            else else
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:System.out.println
            end
            Note right of sequence.ConditionalTestSample: 서브 메소드 콜
            alt 1 == 5
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:서브 메소드 콜:newMethod
            else else
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:System.out.println
            end
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }
}