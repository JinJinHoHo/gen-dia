# GenDia

작동 방식 :

* markdown에 sequenceDiagram 표기 추가
* 첫라인에 대상 클래스및 시작메소드 표기

[//]: # (    ```mermaid)

    sequenceDiagram
    ##class:클래스A,클래스B,클래스C
    ##startPoint:클래스A.메소드1,클래스A.메소드2
    
    mermaid 표기법에 따른 다이어그램......

[//]: # (    ```)
CodeDoc
info
lang
diagram
params
param1

```mermaid
sequenceDiagram

    autonumber
    actor User
    participant Alice


    activate Alice
    User->>+Alice:request
    
        
        Alice->>+John: Hello John, how are you?
        
            activate John
                loop 무한루프
                    John-->John: Great!
                end
                John-->>+Alice:return
            deactivate John
            
        Alice->>D: Hi!
    deactivate Alice
        activate D
            D->>E: Hi!
        deactivate D

        activate John
        John->>E: Hi!
        deactivate John
    
        Alice-->>+User:response
    deactivate Alice
```

```mermaid
sequenceDiagram
    autonumber

    actor  User
    participant testData.sequence.ConditionalTestSample as ConditionalTestSample

    User->>testData.sequence.ConditionalTestSample:Call/testIfElse
    Note right of testData.sequence.ConditionalTestSample: 분기 처리 타입2
    alt i == 5
        testData.sequence.ConditionalTestSample->>testData.sequence.ConditionalTestSample:서브 메소드 콜/newMethod
    else else
        testData.sequence.ConditionalTestSample->>testData.sequence.ConditionalTestSample:프린트/newMethod
    end

```