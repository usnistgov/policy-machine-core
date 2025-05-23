package gov.nist.csd.pm.core.pap.function.routine;

import gov.nist.csd.pm.core.pap.function.AdminFunction;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;

import java.util.List;

public abstract class Routine<R, A extends Args> extends AdminFunction<R, A> {

    public Routine(String name, List<FormalParameter<?>> formalParameters) {
        super(name, formalParameters);
    }

}
