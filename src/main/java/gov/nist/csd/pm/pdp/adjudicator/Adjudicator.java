package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyDeserializer;
import gov.nist.csd.pm.policy.PolicySerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.review.PolicyReview;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.*;

public class Adjudicator implements Policy {

    private final UserContext userCtx;
    private final PrivilegeChecker privilegeChecker;

    private final AdjudicatorGraph adjudicatorGraph;
    private final AdjudicatorProhibitions adjudicatorProhibitions;
    private final AdjudicatorObligations adjudicatorObligations;
    private final AdjudicatorUserDefinedPML adjudicatorUserDefinedPML;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReview policyReview) {
        this.userCtx = userCtx;
        this.privilegeChecker = new PrivilegeChecker(pap, policyReview);

        adjudicatorGraph = new AdjudicatorGraph(userCtx, pap, privilegeChecker);
        adjudicatorProhibitions = new AdjudicatorProhibitions(userCtx, pap, privilegeChecker);
        adjudicatorObligations = new AdjudicatorObligations(userCtx, pap, privilegeChecker);
        adjudicatorUserDefinedPML = new AdjudicatorUserDefinedPML(userCtx, pap, privilegeChecker);
    }

    public PrivilegeChecker getAccessRightChecker() {
        return privilegeChecker;
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
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), SERIALIZE_POLICY);

        return null;
    }

    @Override
    public void deserialize(UserContext author, String input, PolicyDeserializer policyDeserializer)
            throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), DESERIALIZE_POLICY);
    }

    @Override
    public void reset() throws PMException {
        privilegeChecker.check(userCtx, AdminPolicyNode.ADMIN_POLICY_TARGET.nodeName(), RESET);
    }
}
