package gov.nist.csd.pm.common.executable;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class AdminExecutable<T> implements Serializable {

    private static final long serialVersionUID = 1L;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminExecutable<?> that)) return false;
	    return Objects.equals(name, that.name) && Objects.equals(operandNames, that.operandNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operandNames);
    }
}
