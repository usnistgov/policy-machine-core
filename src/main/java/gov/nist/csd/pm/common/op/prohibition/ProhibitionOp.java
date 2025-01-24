package gov.nist.csd.pm.common.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.*;

public abstract class ProhibitionOp extends Operation<Void> {

    public static final String SUBJECT_OPERAND = "subject";
    public static final String ARSET_OPERAND = "arset";
    public static final String INTERSECTION_OPERAND = "intersection";
    public static final String CONTAINERS_OPERAND = "containers";

    private String processReqCap;
    private String reqCap;

    public ProhibitionOp(String opName, String processReqCap, String reqCap) {
        super(
                opName,
                List.of(NAME_OPERAND, SUBJECT_OPERAND, ARSET_OPERAND, INTERSECTION_OPERAND, CONTAINERS_OPERAND)
        );

        this.processReqCap = processReqCap;
        this.reqCap = reqCap;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {
        ProhibitionSubject subject = (ProhibitionSubject) operands.get(SUBJECT_OPERAND);

        if (subject.isNode()) {
            privilegeChecker.check(userCtx, subject.getNodeId(), reqCap);
        } else {
            privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), processReqCap);
        }

        // check that the user can create a prohibition for each container in the condition
        Collection<ContainerCondition> containers = (Collection<ContainerCondition>) operands.get(CONTAINERS_OPERAND);
        for (ContainerCondition contCond : containers) {
            privilegeChecker.check(userCtx, contCond.getId(), reqCap);

            // there is another access right needed if the condition is a complement since it applies to a greater
            // number of nodes
            if (contCond.isComplement()) {
                privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), reqCap);
            }
        }
    }
}
