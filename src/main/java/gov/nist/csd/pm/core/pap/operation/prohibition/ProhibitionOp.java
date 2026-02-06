package gov.nist.csd.pm.core.pap.operation.prohibition;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.type.ContainerConditionType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ProhibitionSubjectArgType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.Collection;
import java.util.List;

public abstract class ProhibitionOp extends AdminOperation<Void> {

    public static final FormalParameter<ProhibitionSubject> SUBJECT_PARAM = new FormalParameter<>("subject", new ProhibitionSubjectArgType());
    public static final FormalParameter<Boolean> INTERSECTION_PARAM = new FormalParameter<>("intersection", BOOLEAN_TYPE);
    public static final FormalParameter<List<ContainerCondition>> CONTAINERS_PARAM = new FormalParameter<>("containers", new ListType<>(new ContainerConditionType()));

    public ProhibitionOp(String name,
                         List<FormalParameter<?>> parameters,
                         RequiredCapability... requiredCapabilities) {
        super(name, VOID_TYPE, parameters, List.of(requiredCapabilities));
    }

    static boolean checkSubject(PolicyQuery policyQuery, UserContext userCtx, ProhibitionSubject subject, AdminAccessRight ar) throws PMException {
        if (subject.isNode()) {
            return policyQuery.access()
                .computePrivileges(userCtx, new TargetContext(subject.getNodeId()))
                .contains(ar.toString());
        } else {
            return policyQuery.access()
                .computePrivileges(userCtx, new TargetContext(AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId()))
                .contains(ar.toString());
        }
    }

    static boolean checkContainers(PolicyQuery policyQuery, UserContext userCtx, Collection<ContainerCondition> containers,
                                   AdminAccessRight inclusionAr, AdminAccessRight exclusionAr) throws PMException {
        for (ContainerCondition contCond : containers) {
            boolean ok;
            if (contCond.isComplement()) {
                ok = policyQuery.access()
                    .computePrivileges(userCtx, new TargetContext(contCond.getId()))
                    .contains(exclusionAr.toString());
            } else {
                ok = policyQuery.access()
                    .computePrivileges(userCtx, new TargetContext(contCond.getId()))
                    .contains(inclusionAr.toString());
            }

            if (!ok) {
                return false;
            }
        }

        return true;
    }
}
