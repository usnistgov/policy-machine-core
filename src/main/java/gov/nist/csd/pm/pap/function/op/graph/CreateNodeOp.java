package gov.nist.csd.pm.pap.function.op.graph;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.op.graph.GraphOp.DESCENDANTS_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.graph.CreateNodeOp.CreateNodeOpArgs;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public abstract class CreateNodeOp extends GraphOp<Long, CreateNodeOpArgs> {

    public static final FormalParameter<String> NAME_ARG = new FormalParameter<>("name", STRING_TYPE);

    private final String ar;

    public CreateNodeOp(String name, List<FormalParameter<?>> formalParameters, String ar) {
        super(name, formalParameters);
        this.ar = ar;
    }

    public static class CreateNodeOpArgs extends Args {
        private final String name;
        private final List<Long> descendantIds;

        public CreateNodeOpArgs(String name, List<Long> descendantIds) {
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
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, CreateNodeOpArgs args) throws PMException {
        List<Long> descendants = args.getDescendantIds();
        if (descendants != null && !descendants.isEmpty()) {
            for (Long nodeId : descendants) {
                privilegeChecker.check(userCtx, nodeId, ar);
            }
        }
    }
}
