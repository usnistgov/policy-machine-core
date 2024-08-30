package gov.nist.csd.pm.pdp.query;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.obligation.EventPattern;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.AdminAccessRights;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.pattern.operand.OperandPatternExpression;
import gov.nist.csd.pm.pap.query.ObligationsQuerier;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.op.AdminAccessRights.GET_OBLIGATION;

public class ObligationsQueryAdjudicator extends ObligationsQuerier {

    private final UserContext userCtx;
    private final PAP pap;
    private final PrivilegeChecker privilegeChecker;

    public ObligationsQueryAdjudicator(UserContext userCtx, PAP pap, PrivilegeChecker privilegeChecker) {
        super(pap.query());
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
    public Collection<Obligation> getObligationsWithAuthor(String user) throws PMException {
        privilegeChecker.check(userCtx, user, AdminAccessRights.REVIEW_POLICY);

        return pap.query().obligations().getObligationsWithAuthor(user);
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
