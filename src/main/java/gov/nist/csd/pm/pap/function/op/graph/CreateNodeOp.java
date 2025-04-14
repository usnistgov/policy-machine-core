package gov.nist.csd.pm.pap.function.op.graph;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.CreateNodeOp.CreateNodeOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public abstract class CreateNodeOp extends GraphOp<Long, CreateNodeOpArgs> {

    private final String ar;

    public CreateNodeOp(String name, List<FormalParameter<?>> formalParameters, String ar) {
        super(name, formalParameters);
        this.ar = ar;
    }

    public static class CreateNodeOpArgs extends Args {
        private final String name;
        private final List<Long> descendantIds;

        public CreateNodeOpArgs(String name, List<Long> descendantIds) {
            super(Map.of(
                NAME_PARAM, name,
                DESCENDANTS_PARAM, descendantIds
            ));

            this.name = name;
            this.descendantIds = descendantIds;
        }

        public String getName() {
            return name;
        }

        public List<Long> getDescendantIds() {
            return descendantIds != null ? descendantIds : List.of();
        }
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, CreateNodeOpArgs args) throws PMException {
        List<Long> descendants = args.getDescendantIds();
        if (descendants != null && !descendants.isEmpty()) {
            for (Long nodeId : descendants) {
                pap.privilegeChecker().check(userCtx, nodeId, ar);
            }
        }
    }
}
