package gov.nist.csd.pm.core.pap.function.op.prohibition;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROHIBITION_WITH_COMPLEMENT_CONTAINER;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.LONG_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.BOOLEAN_TYPE;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubjectType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.op.Operation;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.function.op.prohibition.ProhibitionOp.ProhibitionOpArgs;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class ProhibitionOp extends Operation<Void, ProhibitionOpArgs> {

    public static final FormalParameter<Object> SUBJECT_PARAM = new FormalParameter<>("subject", ANY_TYPE);
    public static final FormalParameter<List<String>> ARSET_PARAM = new FormalParameter<>("arset", ListType.of(STRING_TYPE));
    public static final FormalParameter<Boolean> INTERSECTION_PARAM = new FormalParameter<>("intersection", BOOLEAN_TYPE);
    public static final FormalParameter<Map<Long, Boolean>> CONTAINERS_PARAM = new FormalParameter<>("containers", MapType.of(LONG_TYPE, BOOLEAN_TYPE));

    public ProhibitionOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, formalParameters);
    }

    @Override
    protected ProhibitionOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        ProhibitionSubject subject = (ProhibitionSubject) prepareArg(SUBJECT_PARAM, argsMap);
        AccessRightSet arset = new AccessRightSet(prepareArg(ARSET_PARAM, argsMap));
        Boolean intersection = prepareArg(INTERSECTION_PARAM, argsMap);
        Map<Long, Boolean> containers = prepareArg(CONTAINERS_PARAM, argsMap);
        List<ContainerCondition> containerConditions = new ArrayList<>();
        for (var container : containers.entrySet()) {
            containerConditions.add(new ContainerCondition(container.getKey(), container.getValue()));
        }
        return new ProhibitionOpArgs(name, subject, arset, intersection, containerConditions);
    }

    protected void checkSubject(PAP pap, UserContext userCtx, ProhibitionSubject subject, String nodeAR, String processAR) throws PMException {
        if (subject.isNode()) {
            pap.privilegeChecker().check(userCtx, subject.getNodeId(), nodeAR);
        } else {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), processAR);
        }
    }

    protected void checkContainers(PAP pap, UserContext userCtx, Collection<ContainerCondition> containers, String nodeAR, String complementAR) throws PMException {
        if (containers != null) {
            for (ContainerCondition contCond : containers) {
                pap.privilegeChecker().check(userCtx, contCond.getId(), nodeAR);
                if (contCond.isComplement()) {
                    pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), complementAR);
                }
            }
        }
    }

    public static class ProhibitionOpArgs extends Args {
        private String name;
        private ProhibitionSubject subject;
        private AccessRightSet arset;
        private Boolean intersection;
        private List<ContainerCondition> containers;

        public ProhibitionOpArgs(String name, ProhibitionSubject subject, AccessRightSet arset, Boolean intersection, List<ContainerCondition> containers) {
            super(Map.of(
                NAME_PARAM, name,
                SUBJECT_PARAM, subject,
                ARSET_PARAM, arset,
                INTERSECTION_PARAM, intersection,
                CONTAINERS_PARAM, containers
            ));

            this.name = name;
            this.subject = subject;
            this.arset = arset;
            this.intersection = intersection;
            this.containers = containers;
        }

        public ProhibitionOpArgs(String name) {
            this.name = name;
            this.subject = null;
            this.arset = null;
            this.intersection = null;
            this.containers = null;
        }

        public String getName() { return name; }
        public ProhibitionSubject getSubject() { return subject; }
        public AccessRightSet getArset() { return arset; }
        public Boolean getIntersection() { return intersection; }
        public List<ContainerCondition> getContainers() { return containers; }
    }

}
