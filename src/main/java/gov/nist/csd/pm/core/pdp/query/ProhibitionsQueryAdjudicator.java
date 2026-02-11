package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.NodeProhibition;
import gov.nist.csd.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.operation.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import java.util.Collection;

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
    public Collection<Prohibition> getNodeProhibitions(long nodeId) throws PMException {
        return filterProhibitions(pap.query().prohibitions().getNodeProhibitions(nodeId));
    }

    @Override
    public Collection<Prohibition> getProcessProhibitions(String process) throws PMException {
        return filterProhibitions(pap.query().prohibitions().getProcessProhibitions(process));
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
        switch (prohibition) {
            case NodeProhibition nodeProhibition ->
                check(userCtx, new TargetContext(nodeProhibition.getNodeId()), AdminAccessRight.ADMIN_PROHIBITION_LIST);
            case ProcessProhibition processProhibition ->
                check(userCtx, new TargetContext(processProhibition.getUserId()), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }

        // check user has access to each attribute
        for (long inc : prohibition.getInclusionSet()) {
            check(userCtx, new TargetContext(inc), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }

        for (long exc : prohibition.getExclusionSet()) {
            check(userCtx, new TargetContext(exc), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }
    }
}
