package gov.nist.csd.pm.core.pap.function.op;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import java.util.List;

public abstract non-sealed class ResourceOperation extends Operation<Void> {

    public ResourceOperation(String name,
                             List<FormalParameter<?>> parameters) {
        super(name, parameters);
    }

    @Override
    public final Void execute(PAP pap, Args args) throws PMException {
        return null;
    }
}
