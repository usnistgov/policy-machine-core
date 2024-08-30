package gov.nist.csd.pm.pap.op.graph;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DeleteNodeOp extends GraphOp {

    private String reqCap;
    private String descsReqCap;

    public DeleteNodeOp(String name, String reqCap, String descsReqCap) {
        super(
                name,
                List.of(NAME_OPERAND, TYPE_OPERAND, DESCENDANTS_OPERAND),
                List.of(NAME_OPERAND, DESCENDANTS_OPERAND)
        );

        this.reqCap = reqCap;
        this.descsReqCap = descsReqCap;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        privilegeChecker.check(userCtx, (String) operands.get(NAME_OPERAND), reqCap);

        Collection<String> descs = (Collection<String>) operands.get(DESCENDANTS_OPERAND);
        for (String desc : descs) {
            privilegeChecker.check(userCtx, desc, descsReqCap);
        }
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().graph().deleteNode((String) operands.get(NAME_OPERAND));

        return null;
    }
}
