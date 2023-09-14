package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

class Adjudicator implements Policy {

    private final UserContext userCtx;
    private final PAP pap;
    private final AccessRightChecker accessRightChecker;

    private final AdjudicatorGraph adjudicatorGraph;
    private final AdjudicatorProhibitions adjudicatorProhibitions;
    private final AdjudicatorObligations adjudicatorObligations;
    private final AdjudicatorUserDefinedPML adjudicatorUserDefinedPML;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.accessRightChecker = new AccessRightChecker(pap, policyReviewer);

        adjudicatorGraph = new AdjudicatorGraph(userCtx, pap, accessRightChecker);
        adjudicatorProhibitions = new AdjudicatorProhibitions(userCtx, pap, accessRightChecker);
        adjudicatorObligations = new AdjudicatorObligations(userCtx, pap, accessRightChecker);
        adjudicatorUserDefinedPML = new AdjudicatorUserDefinedPML(userCtx, pap, accessRightChecker);
    }

    @Override
    public AdjudicatorGraph graph() {
        return adjudicatorGraph;
    }

    @Override
    public AdjudicatorProhibitions prohibitions() {
        return adjudicatorProhibitions;
    }

    @Override
    public AdjudicatorObligations obligations() {
        return adjudicatorObligations;
    }

    @Override
    public AdjudicatorUserDefinedPML userDefinedPML() {
        return adjudicatorUserDefinedPML;
    }

    @Override
    public String serialize(PolicySerializer serializer) throws PMException {
        accessRightChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), SERIALIZE_POLICY);

        return null;
    }

    @Override
    public void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer)
            throws PMException {
        accessRightChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), DESERIALIZE_POLICY);
    }

    @Override
    public void reset() throws PMException {
        accessRightChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), RESET);
    }
}
