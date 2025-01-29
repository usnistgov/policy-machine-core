package gov.nist.csd.pm.common.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DissociateOp extends GraphOp<Void> {

    public DissociateOp() {
        super("dissociate",
                List.of(UA_OPERAND, TARGET_OPERAND),
                List.of(UA_OPERAND, TARGET_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().dissociate(
                (long) operands.get(UA_OPERAND),
                (long) operands.get(TARGET_OPERAND)
        );

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (long) operands.get(UA_OPERAND), DISSOCIATE);
        privilegeChecker.check(userCtx, (long) operands.get(TARGET_OPERAND), DISSOCIATE_FROM);
    }

    @Override
    public EventContext toEventContext(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        Map<String, Object> operandsWithNames = new HashMap<>();

        long uaId = (long) operands.get(UA_OPERAND);
        long targetId = (long) operands.get(TARGET_OPERAND);

        operandsWithNames.put(UA_OPERAND, pap.query().graph().getNodeById(uaId).getName());
        operandsWithNames.put(TARGET_OPERAND, pap.query().graph().getNodeById(targetId).getName());

        return new EventContext(
                pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                userCtx.getProcess(),
                this,
                operandsWithNames
        );
    }
}
