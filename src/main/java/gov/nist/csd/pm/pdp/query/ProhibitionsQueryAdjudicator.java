package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.GET_PROCESS_PROHIBITIONS;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.GET_PROHIBITIONS;

public class ProhibitionsQueryAdjudicator extends Adjudicator implements ProhibitionsQuery {

    public ProhibitionsQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<Prohibition> getProhibitions() throws PMException {
        Collection<Prohibition> prohibitions = pap.query().prohibitions().getProhibitions();
        return filterProhibitions(prohibitions);
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithSubject(ProhibitionSubject subject) throws PMException {
        return filterProhibitions(pap.query().prohibitions().getProhibitionsWithSubject(subject));
    }

    @Override
    public Prohibition getProhibition(String name) throws PMException {
        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        // check user has access to subject
        if (prohibition.getSubject().isNode()) {
            pap.privilegeChecker().check(userCtx, prohibition.getSubject().getNodeId(), GET_PROHIBITIONS);
        } else {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), GET_PROCESS_PROHIBITIONS);
        }

        // check user has access to each container condition
        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            pap.privilegeChecker().check(userCtx, containerCondition.getId(), GET_PROHIBITIONS);
        }

        return prohibition;
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) throws PMException {
        pap.privilegeChecker().check(this.userCtx, subjectId, AdminAccessRights.REVIEW_POLICY);

        return pap.query().prohibitions().getInheritedProhibitionsFor(subjectId);
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(long containerId) throws PMException {
        pap.privilegeChecker().check(this.userCtx, containerId, AdminAccessRights.REVIEW_POLICY);

        return pap.query().prohibitions().getProhibitionsWithContainer(containerId);
    }

    private Collection<Prohibition> filterProhibitions(Collection<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                // check user has access to subject prohibitions
                if (prohibition.getSubject().isNode()) {
                    pap.privilegeChecker().check(userCtx, prohibition.getSubject().getNodeId(), GET_PROHIBITIONS);
                } else {
                    pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_OBJECT.nodeId(), GET_PROCESS_PROHIBITIONS);
                }

                // check user has access to each target prohibitions
                for (ContainerCondition containerCondition : prohibition.getContainers()) {
                    pap.privilegeChecker().check(userCtx, containerCondition.getId(), GET_PROHIBITIONS);
                }

                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return prohibitions;
    }
}
