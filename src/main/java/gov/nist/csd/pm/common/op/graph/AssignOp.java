package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends GraphOp {

    public AssignOp() {
        super(
                "assign",
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND),
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        String asc = (String) operands.get(ASCENDANT_OPERAND);
        List<String> descs = (List<String>) operands.get(DESCENDANTS_OPERAND);

        pap.modify().graph().assign(asc, descs);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (String) operands.get(ASCENDANT_OPERAND), ASSIGN);
        privilegeChecker.check(userCtx, (List<String>) operands.get(DESCENDANTS_OPERAND), ASSIGN_TO);
    }
}
