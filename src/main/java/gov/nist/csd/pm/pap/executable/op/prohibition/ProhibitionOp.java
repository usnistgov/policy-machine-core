package gov.nist.csd.pm.pap.executable.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.executable.op.Operation;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.pap.executable.op.prohibition.CreateProhibitionOp.CONTAINERS_ARG;
import static gov.nist.csd.pm.pap.executable.op.prohibition.CreateProhibitionOp.SUBJECT_ARG;

public abstract class ProhibitionOp extends Operation<Void> {

    public static final FormalArg<ProhibitionSubject> SUBJECT_ARG = new FormalArg<>("subject", ProhibitionSubject.class);
    public static final FormalArg<AccessRightSet> ARSET_ARG = new FormalArg<>("arset", AccessRightSet.class);
    public static final FormalArg<Boolean> INTERSECTION_ARG = new FormalArg<>("intersection", Boolean.class);
    public static final FormalArg<ContainerConditionsList> CONTAINERS_ARG = new FormalArg<>("containers", ContainerConditionsList.class);

    private final String processReqCap;
    private final String reqCap;

    public ProhibitionOp(String opName, String processReqCap, String reqCap) {
        super(
                opName,
                List.of(
                        NAME_ARG,
                        SUBJECT_ARG,
                        ARSET_ARG,
                        INTERSECTION_ARG,
                        CONTAINERS_ARG
                )
        );

        this.processReqCap = processReqCap;
        this.reqCap = reqCap;
    }

    public ActualArgs actualArgs(String name, ProhibitionSubject subject, AccessRightSet arset,
                                 Boolean intersection, ContainerConditionsList containers) {
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(NAME_ARG, name);
        actualArgs.put(SUBJECT_ARG, subject);
        actualArgs.put(ARSET_ARG, arset);
        actualArgs.put(INTERSECTION_ARG, intersection);
        actualArgs.put(CONTAINERS_ARG, containers);
        return actualArgs;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, ActualArgs operands) throws PMException {
        ProhibitionSubject subject = operands.get(SUBJECT_ARG);

        if (subject.isNode()) {
            privilegeChecker.check(userCtx, subject.getNodeId(), reqCap);
        } else {
            privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), processReqCap);
        }

        // check that the user can create a prohibition for each container in the condition
        Collection<ContainerCondition> containers = operands.get(CONTAINERS_ARG);
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
