package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN;
import static gov.nist.csd.pm.pap.AdminAccessRights.ASSIGN_TO;

public class AssignOp extends GraphOp<Void> {

    public AssignOp() {
        super(
                "assign",
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND),
                List.of(ASCENDANT_OPERAND, DESCENDANTS_OPERAND)
        );
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        long asc = (long) operands.get(ASCENDANT_OPERAND);
        List<Long> descs = (List<Long>) operands.get(DESCENDANTS_OPERAND);

        pap.modify().graph().assign(asc, descs);

        return null;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (long) operands.get(ASCENDANT_OPERAND), ASSIGN);
        privilegeChecker.check(userCtx, (List<Long>) operands.get(DESCENDANTS_OPERAND), ASSIGN_TO);
    }

    public static class EventCtx extends EventContext {

        public EventCtx(String user, String process, String ascendantName, List<String> descendantNames) {
            super(user, process, "assign", Map.of(
                    ASCENDANT_OPERAND, ascendantName,
                    DESCENDANTS_OPERAND, descendantNames
            ));
        }
    }
}
