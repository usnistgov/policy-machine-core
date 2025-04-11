package gov.nist.csd.pm.pap.pml.statement.result;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;

public class ValueResult<T> extends StatementResult {

    private T value;
    private ArgType<T> type;

    public ValueResult(T value, ArgType<T> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ArgType<T> getType() {
        return type;
    }

    public void setType(ArgType<T> type) {
        this.type = type;
    }
}
