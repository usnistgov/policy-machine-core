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
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.ObligationsQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import gov.nist.ngac.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A {@link ObligationsQuery} that checks the acting user's admin privileges before delegating to the
 * PAP.
 */
public class ObligationsQueryAdjudicator extends Adjudicator implements ObligationsQuery {

    public ObligationsQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        Collection<Obligation> obligations = pap.query().obligations().getObligations();
        return filterObligations(obligations);
    }

    @Override
    public boolean obligationExists(String name) throws PMException {
        boolean exists = pap.query().obligations().obligationExists(name);
        if (!exists) {
            return false;
        }

        try {
            getObligation(name);
        } catch (UnauthorizedException e) {
            return false;
        }

        return true;
    }

    @Override
    public Obligation getObligation(String name) throws PMException {
        Obligation obligation = pap.query().obligations().getObligation(name);

        check(userCtx, toTargetCtx(obligation.getAuthor()), AdminAccessRight.ADMIN_OBLIGATION_LIST);

        return obligation;
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(NodeUserContext authorCtx) throws PMException {
        check(userCtx, toTargetCtx(authorCtx), AdminAccessRight.ADMIN_OBLIGATION_LIST);

        Collection<Obligation> obligationsWithAuthor = new ArrayList<>(pap.query().obligations().getObligationsWithAuthor(
            authorCtx));
        return filterObligations(obligationsWithAuthor);
    }

    private TargetContext toTargetCtx(NodeUserContext author) throws PMException {
        long id = author.resolveNodeIds(pap.query().graph()).iterator().next();
        return NodeTargetContext.of(id);
    }

    Collection<Obligation> filterObligations(Collection<Obligation> obligations) throws PMException {
        filterUnauthorized(obligations, obligation ->
            check(userCtx, toTargetCtx(obligation.getAuthor()), AdminAccessRight.ADMIN_OBLIGATION_LIST));

        return obligations;
    }
}
