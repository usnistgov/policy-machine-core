package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.operand.ListStringOperandValue;
import gov.nist.csd.pm.common.event.operand.OperandValue;
import gov.nist.csd.pm.common.event.operand.StringOperandValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.PreparedOp;
import gov.nist.csd.pm.common.op.PreparedOperation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Override
    public PreparedOp prepare() {
        return new Prepared();
    }

    static class Prepared extends PreparedOp {

        private long ascendant;
        private List<Long> descendants;

        public Prepared(long ascendant, List<Long> descendants) {
            this.ascendant = ascendant;
            this.descendants = descendants;
        }

        @Override
        public EventContext getEventContext(UserContext userCtx, PAP pap) throws PMException {
            return new EventContext(
                    pap.query().graph().getNodeById(userCtx.getUser()).getName(),
                    userCtx.getProcess(),
                    "assign",
                    Map.of(
                    ASCENDANT_OPERAND, new StringOperandValue(pap.query().graph().getNodeById(ascendant).getName()),
                    DESCENDANTS_OPERAND, ListStringOperandValue.fromIds(pap, descendants)
            ));
        }
    }
}
