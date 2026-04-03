package gov.nist.csd.pm.core.pdp.query;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.query.ObligationsQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetIdContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetNameContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserIdContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserNodeContext;
import gov.nist.csd.pm.core.pap.query.model.context.UsernameContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import gov.nist.csd.pm.core.pdp.adjudication.Adjudicator;
import java.util.ArrayList;
import java.util.Collection;

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
    public Collection<Obligation> getObligationsWithAuthor(UserNodeContext authorCtx) throws PMException {
        check(userCtx, toTargetCtx(authorCtx), AdminAccessRight.ADMIN_OBLIGATION_LIST);

        Collection<Obligation> obligationsWithAuthor = new ArrayList<>(pap.query().obligations().getObligationsWithAuthor(
            authorCtx));
        return filterObligations(obligationsWithAuthor);
    }

    private TargetContext toTargetCtx(UserNodeContext author) {
        return switch (author) {
            case UserIdContext c -> new TargetIdContext(c.userId());
            case UsernameContext c -> new TargetNameContext(c.username());
        };
    }

    private Collection<Obligation> filterObligations(Collection<Obligation> obligations) {
        obligations.removeIf(obligation -> {
            try {
                check(userCtx, toTargetCtx(obligation.getAuthor()), AdminAccessRight.ADMIN_OBLIGATION_LIST);

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
