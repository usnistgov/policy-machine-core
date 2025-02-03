package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class CreateNodeOp extends GraphOp<Long> {

    private final String ar;

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
        Collection<Long> coll = (Collection<Long>) operands.get(DESCENDANTS_OPERAND);
        for (Long l : coll) {
            privilegeChecker.check(userCtx, l, ar);
        }
    }

}
