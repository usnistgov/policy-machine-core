package gov.nist.csd.pm.pap.pml.statement.result;

public class ReturnResult extends StatementResult{

    private Object value;

    public ReturnResult(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
