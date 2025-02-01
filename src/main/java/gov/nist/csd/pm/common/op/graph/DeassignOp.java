package gov.nist.csd.pm.common.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

import static gov.nist.csd.pm.pap.AdminAccessRights.*;

public class DeassignOp extends GraphOp<Void> {

    public DeassignOp() {
        super(
                "deassign",
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND),
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND)
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (Long) operands.get(ASCENDANT_OPERAND), DEASSIGN);
        privilegeChecker.check(userCtx, (List<Long>) operands.get(DESCENDANTS_OPERAND), DEASSIGN_FROM);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        long asc = (Long) operands.get(ASCENDANT_OPERAND);
        List<Long> descs = (List<Long>) operands.get(DESCENDANTS_OPERAND);

        pap.modify().graph().deassign(asc, descs);

        return null;
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String ascendantName, List<String> descendantNames) {
            super(user, process, "deassign", Map.of(
                    ASCENDANT_OPERAND, ascendantName,
                    DESCENDANTS_OPERAND, descendantNames
            ));
        }
    }
}