package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.query.ObligationsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;

import java.util.ArrayList;
import java.util.Collection;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.QUERY_OBLIGATIONS;
import static gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.checkObligationRulePrivileges;

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
        checkObligationRulePrivileges(pap, userCtx, obligation.getRules(), QUERY_OBLIGATIONS, QUERY_OBLIGATIONS);

        return obligation;
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long userId) throws PMException {
        pap.privilegeChecker().check(userCtx, userId, QUERY_OBLIGATIONS);

        Collection<Obligation> obligationsWithAuthor = new ArrayList<>(pap.query().obligations().getObligationsWithAuthor(userId));
        return filterObligations(obligationsWithAuthor);
    }

    private Collection<Obligation> filterObligations(Collection<Obligation> obligations) {
        obligations.removeIf(obligation -> {
            try {
                checkObligationRulePrivileges(pap, userCtx, obligation.getRules(), QUERY_OBLIGATIONS, QUERY_OBLIGATIONS);
                return false;
            } catch (UnauthorizedException e) {
                return true;
            } catch (PMException e) {
                throw new RuntimeException(e);
            }
        });

        return obligations;
    }
}
