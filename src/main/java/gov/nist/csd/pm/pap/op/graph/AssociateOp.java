package gov.nist.csd.pm.pap.op.graph;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.*;

public class AssociateOp extends GraphOp {

    public AssociateOp() {
        super("associate",
                List.of(UA_OPERAND, TARGET_OPERAND, ARSET_OPERAND),
                List.of(UA_OPERAND, TARGET_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().associate(
                (String) operands.get(UA_OPERAND),
                (String) operands.get(TARGET_OPERAND),
                (AccessRightSet) operands.get(ARSET_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        PrivilegeChecker.check(pap, userCtx, (String) operands.get(UA_OPERAND), ASSOCIATE);
        PrivilegeChecker.check(pap, userCtx, (String) operands.get(TARGET_OPERAND), ASSOCIATE_TO);

    }
}
