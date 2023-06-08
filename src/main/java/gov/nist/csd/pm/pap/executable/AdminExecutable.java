package gov.nist.csd.pm.pap.executable;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.List;
import java.util.Map;

public abstract class AdminExecutable<T> {

    protected final String name;
    protected final List<String> operandNames;

    public AdminExecutable(String name, List<String> operandNames) {
        this.name = name;
        this.operandNames = operandNames;
    }

    public abstract T execute(PAP pap, Map<String, Object> operands) throws PMException;

    public String getName() {
        return name;
    }

    public List<String> getOperandNames() {
        return operandNames;
    }
}
