/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pdp.query;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.ProhibitionsQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.Collection;

/**
 * A {@link ProhibitionsQuery} that checks the acting user's admin privileges before delegating to the
 * PAP.
 */
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
                check(userCtx, NodeTargetContext.of(nodeProhibition.getNodeId()), AdminAccessRight.ADMIN_PROHIBITION_LIST);
            case ProcessProhibition processProhibition ->
                check(userCtx, NodeTargetContext.of(processProhibition.getUserId()), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }

        // check user has access to each attribute
        for (long inc : prohibition.getInclusionSet()) {
            check(userCtx, NodeTargetContext.of(inc), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }

        for (long exc : prohibition.getExclusionSet()) {
            check(userCtx, NodeTargetContext.of(exc), AdminAccessRight.ADMIN_PROHIBITION_LIST);
        }
    }
}
