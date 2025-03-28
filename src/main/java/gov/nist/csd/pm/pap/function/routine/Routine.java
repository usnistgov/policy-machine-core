package gov.nist.csd.pm.pap.function.routine;

import gov.nist.csd.pm.pap.function.AdminFunction;
import gov.nist.csd.pm.pap.function.arg.FormalArg;

import java.util.List;

public abstract class Routine<T> extends AdminFunction<T> {

    public Routine(String name, List<FormalArg<?>> formalArgs) {
        super(name, formalArgs);
    }
}
