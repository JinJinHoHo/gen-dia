package testData;

public class FoldingTestData {
    public static void main(String[] args) {
        System.out.println("<fold text='https://en.wikipedia.org/'>simple:website</fold>");
    }

    public static void main1(String[] args) {
        System.out.println("<fold text='This is the value that could be looked up with the key \"key with spaces\".'>simple:key with spaces</fold>");
    }

    public static void main2(String[] args) {
        System.out.println("<fold text='Welcome to \n          Wikipedia!'>simple:message</fold>");
    }
}
