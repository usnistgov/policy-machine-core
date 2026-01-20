package gov.nist.csd.pm.core.pap.operation.prohibition;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.arg.type.ContainerConditionType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ProhibitionSubjectArgType;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Collection;
import java.util.List;

public abstract class ProhibitionOp extends AdminOperation<Void> {

    public static final FormalParameter<ProhibitionSubject> SUBJECT_PARAM = new FormalParameter<>("subject", new ProhibitionSubjectArgType());
    public static final FormalParameter<Boolean> INTERSECTION_PARAM = new FormalParameter<>("intersection", BOOLEAN_TYPE);
    public static final FormalParameter<List<ContainerCondition>> CONTAINERS_PARAM = new FormalParameter<>("containers", new ListType<>(new ContainerConditionType()));

    public ProhibitionOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, BasicTypes.VOID_TYPE,formalParameters);
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
