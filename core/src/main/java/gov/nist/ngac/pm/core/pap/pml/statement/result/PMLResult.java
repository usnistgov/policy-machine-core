package gov.nist.ngac.pm.core.pap.pml.statement.result;

/**
 * Represents a break, continue, or return outcome, built via its static factories.
 */
public class PMLResult {

    private boolean isBreak;
    private boolean isContinue;
    private boolean isReturn;
    private Object value;

    private PMLResult() {}

    /**
     * Builds a break result.
     */
    public static PMLResult breakResult() {
        PMLResult result = new PMLResult();
        result.isBreak = true;
        return result;
    }

    /**
     * Builds a continue result.
     */
    public static PMLResult continueResult() {
        PMLResult result = new PMLResult();
        result.isContinue = true;
        return result;
    }

    /**
     * Builds a return result carrying the given value.
     */
    public static PMLResult returnResult(Object o) {
        PMLResult result = new PMLResult();
        result.isReturn = true;
        result.value = o;
        return result;
    }

    public void isValue(Object o) {

    }
}
