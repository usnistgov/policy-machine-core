package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class Adjudicator implements Policy {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    private final GraphAdjudicator graphAdjudicator;
    private final ProhibitionsAdjudicator prohibitionsAdjudicator;
    private final ObligationsAdjudicator obligationsAdjudicator;
    private final UserDefinedPMLAdjudicator userDefinedPMLAdjudicator;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);

        graphAdjudicator = new GraphAdjudicator(userCtx, pap, accessRightChecker);
        prohibitionsAdjudicator = new ProhibitionsAdjudicator(userCtx, pap, accessRightChecker);
        obligationsAdjudicator = new ObligationsAdjudicator(userCtx, pap, accessRightChecker);
        userDefinedPMLAdjudicator = new UserDefinedPMLAdjudicator(userCtx, pap, accessRightChecker);
    }

    @Override
    public GraphAdjudicator graph() {
        return graphAdjudicator;
    }

    @Override
    public ProhibitionsAdjudicator prohibitions() {
        return prohibitionsAdjudicator;
    }

    @Override
    public ObligationsAdjudicator obligations() {
        return obligationsAdjudicator;
    }

    @Override
    public UserDefinedPMLAdjudicator userDefinedPML() {
        return userDefinedPMLAdjudicator;
    }

    @Override
    public PolicySerializer serialize() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, TO_STRING);

        return null;
    }

    @Override
    public PolicyDeserializer deserialize() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, FROM_STRING);

        return null;
    }

    @Override
    public void reset() throws PMException {
        accessRightChecker.check(userCtx, SUPER_PC_REP, RESET);
    }
}
