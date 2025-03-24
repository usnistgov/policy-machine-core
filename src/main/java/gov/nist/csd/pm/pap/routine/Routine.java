package gov.nist.csd.pm.pap.routine;

import gov.nist.csd.pm.pap.executable.AdminExecutable;

import java.util.List;

public abstract class Routine<T> extends AdminExecutable<T> {

    public Routine(String name, List<String> operandNames) {
        super(name, operandNames);
    }
}
