package gov.nist.csd.pm.common.op.graph;


import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

public abstract class DeleteNodeOp extends GraphOp {

    private String reqCap;
    private String descsReqCap;

    public DeleteNodeOp(String name, String reqCap, String descsReqCap) {
        super(
                name,
                List.of(ID_OPERAND, TYPE_OPERAND, DESCENDANTS_OPERAND),
                List.of(ID_OPERAND, DESCENDANTS_OPERAND)
        );

        this.reqCap = reqCap;
        this.descsReqCap = descsReqCap;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (long) operands.get(ID_OPERAND), reqCap);

        Collection<Long> descs = (Collection<Long>) operands.get(DESCENDANTS_OPERAND);
        for (Long desc : descs) {
            privilegeChecker.check(userCtx, desc, descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().deleteNode((long) operands.get(ID_OPERAND));

        return null;
    }

    @Override
    public EventContext toEventContext(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {
        Map<String, Object> operandsWithNames = new HashMap<>();

        long asc = (long) operands.get(ID_OPERAND);
        List<Long> descs = (List<Long>) operands.get(DESCENDANTS_OPERAND);
        List<String> descNames = new ArrayList<>();
        for (Long desc : descs) {
            descNames.add(pap.query().graph().getNodeById(desc).getName());
        }

        operandsWithNames.put(NAME_OPERAND, pap.query().graph().getNodeById(asc).getName());
        operandsWithNames.put(DESCENDANTS_OPERAND, descNames);

        return new EventContext(
                userCtx.getUser(),
                userCtx.getProcess(),
                this,
                operandsWithNames
        );
    }
}
