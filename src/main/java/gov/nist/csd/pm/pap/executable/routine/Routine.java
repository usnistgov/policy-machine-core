package gov.nist.csd.pm.pap.executable.routine;

import gov.nist.csd.pm.pap.executable.AdminExecutable;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;

import java.util.List;

public abstract class Routine<T> extends AdminExecutable<T> {

    public Routine(String name, List<FormalArg<?>> formalArgs) {
        super(name, formalArgs);
    }
}
