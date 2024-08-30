package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;

public class DissociateOp extends GraphOp {

    public DissociateOp() {
        super("dissociate",
                List.of(UA_OPERAND, TARGET_OPERAND),
                List.of(UA_OPERAND, TARGET_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().dissociate(
                (String) operands.get(UA_OPERAND),
                (String) operands.get(TARGET_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (String) operands.get(UA_OPERAND), DISSOCIATE);
        privilegeChecker.check(userCtx, (String) operands.get(TARGET_OPERAND), DISSOCIATE_FROM);
    }
}
