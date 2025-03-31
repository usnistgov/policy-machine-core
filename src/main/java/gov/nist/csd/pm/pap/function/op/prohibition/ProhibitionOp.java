package gov.nist.csd.pm.pap.function.op.prohibition;

import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.booleanType;
import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.listType;
import static gov.nist.csd.pm.pap.function.arg.type.SupportedArgTypes.stringType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class ProhibitionOp extends Operation<Void> {

    public static final FormalArg<ProhibitionSubject> SUBJECT_ARG = new FormalArg<>("subject", new ProhibitionSubjectType());
    public static final FormalArg<List<String>> ARSET_ARG = new FormalArg<>("arset", listType(stringType()));
    public static final FormalArg<Boolean> INTERSECTION_ARG = new FormalArg<>("intersection", booleanType());
    public static final FormalArg<List<ContainerCondition>> CONTAINERS_ARG = new FormalArg<>("containers", listType(new ContainerConditionType()));

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

    public Args actualArgs(String name, ProhibitionSubject subject, AccessRightSet arset,
                           Boolean intersection, List<ContainerCondition> containers) {
        Args args = new Args();
        args.put(NAME_ARG, name);
        args.put(SUBJECT_ARG, subject);
        args.put(ARSET_ARG, new ArrayList<>(arset));
        args.put(INTERSECTION_ARG, intersection);
        args.put(CONTAINERS_ARG, containers);
        return args;
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {
        ProhibitionSubject subject = args.get(SUBJECT_ARG);

        if (subject.isNode()) {
            privilegeChecker.check(userCtx, subject.getNodeId(), reqCap);
        } else {
            privilegeChecker.check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), processReqCap);
        }

        // check that the user can create a prohibition for each container in the condition
        Collection<ContainerCondition> containers = args.get(CONTAINERS_ARG);
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
