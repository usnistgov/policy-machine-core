package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

public abstract class CreateNodeOp extends GraphOp {

    private String ar;

    public CreateNodeOp(String name, String ar) {
        super(
                name,
                List.of(NAME_OPERAND, DESCENDANTS_OPERAND),
                List.of(DESCENDANTS_OPERAND)
        );

        this.ar = ar;
    }

    public CreateNodeOp(String name, List<String> nodeOperands, List<String> otherOperands, String ar) {
        super(
                name,
                nodeOperands,
                otherOperands
        );

        this.ar = ar;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        Collection<?> coll = (Collection<?>) operands.get(DESCENDANTS_OPERAND);
        for (Object o : coll) {
            if (o instanceof String strColOp) {
                privilegeChecker.check(userCtx, strColOp, ar);
            }
        }
    }
}
