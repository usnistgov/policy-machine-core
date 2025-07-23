package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.common.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.core.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.Collection;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.QUERY_PROCESS_PROHIBITIONS;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.QUERY_PROHIBITIONS;

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
        checkCanQueryProhibition(prohibition);

        return prohibition;
    }

    @Override
    public boolean prohibitionExists(String name) throws PMException {
        boolean exists = pap.query().prohibitions().prohibitionExists(name);
        if (!exists) {
            return false;
        }

        Prohibition prohibition = pap.query().prohibitions().getProhibition(name);

        try {
            checkCanQueryProhibition(prohibition);
        } catch (UnauthorizedException e) {
            return false;
        }

        return true;
    }

    @Override
    public Collection<Prohibition> getInheritedProhibitionsFor(long subjectId) throws PMException {
        Collection<Prohibition> prohibitions = pap.query().prohibitions()
            .getInheritedProhibitionsFor(subjectId);
        return filterProhibitions(prohibitions);
    }

    @Override
    public Collection<Prohibition> getProhibitionsWithContainer(long containerId) throws PMException {
        Collection<Prohibition> prohibitions = pap.query().prohibitions()
            .getProhibitionsWithContainer(containerId);
        return filterProhibitions(prohibitions);
    }

    private Collection<Prohibition> filterProhibitions(Collection<Prohibition> prohibitions) {
        prohibitions.removeIf(prohibition -> {
            try {
                checkCanQueryProhibition(prohibition);

                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return prohibitions;
    }

    private void checkCanQueryProhibition(Prohibition prohibition) throws PMException {
        if (prohibition.getSubject().isNode()) {
            pap.privilegeChecker().check(userCtx, prohibition.getSubject().getNodeId(), QUERY_PROHIBITIONS);
        } else {
            pap.privilegeChecker().check(userCtx, AdminPolicyNode.PM_ADMIN_PROHIBITIONS.nodeId(), QUERY_PROCESS_PROHIBITIONS);
        }

        // check user has access to each container condition
        for (ContainerCondition containerCondition : prohibition.getContainers()) {
            pap.privilegeChecker().check(userCtx, containerCondition.getId(), QUERY_PROHIBITIONS);
        }
    }
}
