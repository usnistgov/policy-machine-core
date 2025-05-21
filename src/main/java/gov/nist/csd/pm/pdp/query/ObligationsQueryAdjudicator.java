package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.admin.AdminAccessRights;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.arg.ArgPatternExpression;
import gov.nist.csd.pm.pap.query.ObligationsQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.GET_OBLIGATION;
import static gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.checkPatternPrivileges;

public class ObligationsQueryAdjudicator extends Adjudicator implements ObligationsQuery {

    public ObligationsQueryAdjudicator(PAP pap, UserContext userCtx) {
        super(pap, userCtx);
    }

    @Override
    public Collection<Obligation> getObligations() throws PMException {
        Collection<Obligation> obligations = pap.query().obligations().getObligations();
        obligations.removeIf(obligation -> {
            try {
                for (Rule rule : obligation.getRules()) {
                    checkRule(rule);
                }
                return false;
            } catch (PMException e) {
                return true;
            }
        });

        return obligations;
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
        for (Rule rule : obligation.getRules()) {
            checkRule(rule);
        }

        return obligation;
    }

    @Override
    public Collection<Obligation> getObligationsWithAuthor(long userId) throws PMException {
        pap.privilegeChecker().check(userCtx, userId, AdminAccessRights.REVIEW_POLICY);

        return pap.query().obligations().getObligationsWithAuthor(userId);
    }

    private void checkRule(Rule rule) throws PMException {
        EventPattern eventPattern = rule.getEventPattern();

        // check subject
        checkPatternPrivileges(pap, userCtx, eventPattern.getSubjectPattern(), GET_OBLIGATION);

        // cannot check operation as it is not a node

        // check args
        for (Map.Entry<String, List<ArgPatternExpression>> argPattern : eventPattern.getArgPatterns().entrySet()) {
            for (ArgPatternExpression argPatternExpression : argPattern.getValue()) {
                checkPatternPrivileges(pap, userCtx, argPatternExpression, GET_OBLIGATION);
            }
        }
    }
}
