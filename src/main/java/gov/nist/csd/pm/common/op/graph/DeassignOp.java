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

public class DeassignOp extends GraphOp {

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

    @Override
    public EventContext toEventContext(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        Map<String, Object> operandsWithNames = new HashMap<>();

        long asc = (long) operands.get(ASCENDANT_OPERAND);
        List<Long> descs = (List<Long>) operands.get(DESCENDANTS_OPERAND);
        List<String> descNames = new ArrayList<>();
        for (Long desc : descs) {
            descNames.add(pap.query().graph().getNodeById(desc).getName());
        }

        operandsWithNames.put(ASCENDANT_OPERAND, pap.query().graph().getNodeById(asc).getName());
        operandsWithNames.put(DESCENDANTS_OPERAND, descNames);

        return new EventContext(userCtx.getUser(), userCtx.getProcess(), this, operandsWithNames);
    }
}