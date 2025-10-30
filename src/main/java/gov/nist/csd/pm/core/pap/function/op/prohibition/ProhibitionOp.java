package gov.nist.csd.pm.core.pap.function.op.prohibition;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.BOOLEAN_TYPE;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ContainerConditionType;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.ProhibitionSubjectArgType;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.Collection;
import java.util.List;

public abstract class ProhibitionOp extends Operation<Void> {

    public static final FormalParameter<ProhibitionSubject> SUBJECT_PARAM = new FormalParameter<>("subject", new ProhibitionSubjectArgType());
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<Boolean> INTERSECTION_PARAM = new FormalParameter<>("intersection", BOOLEAN_TYPE);
    public static final FormalParameter<List<ContainerCondition>> CONTAINERS_PARAM = new FormalParameter<>("containers", new ListType<>(new ContainerConditionType()));

    public ProhibitionOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, formalParameters);
    }

    protected void checkSubject(PAP pap, UserContext userCtx, ProhibitionSubject subject, String nodeAR, String processAR) throws PMException {
        if (subject.isNode()) {
            pap.privilegeChecker().check(userCtx, subject.getNodeId(), nodeAR);
        } else {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), processAR);
        }
    }

    protected void checkContainers(PAP pap, UserContext userCtx, Collection<ContainerCondition> containers, String nodeAR, String complementAR) throws PMException {
        for (ContainerCondition contCond : containers) {
            pap.privilegeChecker().check(userCtx, contCond.getId(), nodeAR);
            if (contCond.isComplement()) {
                pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), complementAR);
            }
        }
    }
}
