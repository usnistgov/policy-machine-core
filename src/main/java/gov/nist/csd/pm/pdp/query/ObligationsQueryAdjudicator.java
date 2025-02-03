package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.AdminAccessRights;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.query.ObligationsQuery;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.UnauthorizedException;
import gov.nist.csd.pm.pdp.adjudication.Adjudicator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.GET_OBLIGATION;

public class ObligationsQueryAdjudicator extends Adjudicator implements ObligationsQuery {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public ObligationsQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(privilegeChecker);
        this.userCtx = userCtx;
        this.pap = pap;
        this.privilegeChecker = privilegeChecker;
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
        privilegeChecker.check(userCtx, userId, AdminAccessRights.REVIEW_POLICY);

        return pap.query().obligations().getObligationsWithAuthor(userId);
    }

    private void checkRule(Rule rule) throws PMException {
        EventPattern eventPattern = rule.getEventPattern();

        // check subject
        privilegeChecker.checkPattern(userCtx, eventPattern.getSubjectPattern(), GET_OBLIGATION);

        // cannot check operation as it is not a node

        // check operands
        for (Map.Entry<String, List<OperandPatternExpression>> operandPattern : eventPattern.getOperandPatterns().entrySet()) {
            for (OperandPatternExpression operandPatternExpression : operandPattern.getValue()) {
                privilegeChecker.checkPattern(userCtx, operandPatternExpression, GET_OBLIGATION);
            }
        }
    }
}
