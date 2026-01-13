package gov.nist.csd.pm.core.pap.function.routine;

import gov.nist.csd.pm.core.pap.function.Function;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import java.util.List;

public abstract class Routine<R> extends Function<R> {

    public Routine(String name, List<FormalParameter<?>> parameters) {
        super(name, parameters);
    }
}
