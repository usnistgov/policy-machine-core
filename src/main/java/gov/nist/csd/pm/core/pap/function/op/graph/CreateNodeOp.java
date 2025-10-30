package gov.nist.csd.pm.core.pap.function.op.graph;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.List;

public abstract class CreateNodeOp extends GraphOp<Long> {

    private final String ar;

    public CreateNodeOp(String name, boolean hasDescendantsParam, String ar) {
        super(name, hasDescendantsParam ? List.of(NAME_PARAM, DESCENDANTS_PARAM) : List.of(NAME_PARAM));
        this.ar = ar;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        List<Long> descendants = args.get(DESCENDANTS_PARAM);
        if (descendants != null && !descendants.isEmpty()) {
            for (Long nodeId : descendants) {
                pap.privilegeChecker().check(userCtx, nodeId, ar);
            }
        }
    }
}
