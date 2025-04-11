package gov.nist.csd.pm.pap.pml.statement.result;

public class PMLResult {

    private boolean isBreak;
    private boolean isContinue;
    private boolean isReturn;
    private Object value;

    private PMLResult() {}

    public static PMLResult breakResult() {
        PMLResult result = new PMLResult();
        result.isBreak = true;
        return result;
    }

    public static PMLResult continueResult() {
        PMLResult result = new PMLResult();
        result.isContinue = true;
        return result;
    }

    public static PMLResult returnResult(Object o) {
        PMLResult result = new PMLResult();
        result.isReturn = true;
        result.value = o;
        return result;
    }

    public void isValue(Object o) {

    }
}
