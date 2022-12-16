package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.serializer.PolicyDeserializer;
import gov.nist.csd.pm.policy.serializer.PolicySerializer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.FROM_STRING;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.TO_STRING;

public class Adjudicator extends PolicyAuthor {

    private final UserContext userCtx;
    private final PAP pap;
    private final PolicyReviewer policyReviewer;

    public Adjudicator(UserContext userCtx, PAP pap, PolicyReviewer policyReviewer) {
        this.userCtx = userCtx;
        this.pap = pap;
        this.policyReviewer = policyReviewer;
    }

    @Override
    public GraphAuthor graph() {
        return new Graph(userCtx, pap, policyReviewer);
    }

    @Override
    public ProhibitionsAuthor prohibitions() {
        return new Prohibitions(userCtx, pap, policyReviewer);
    }

    @Override
    public ObligationsAuthor obligations() {
        return new Obligations(userCtx, pap, policyReviewer);
    }

    @Override
    public PALAuthor pal() {
        return new PAL(userCtx, pap, policyReviewer);
    }

    @Override
    public String toString(PolicySerializer policySerializer) throws PMException {
        AccessRightSet accessRights = policyReviewer.getAccessRights(userCtx, SUPER_PC_REP);
        if (!accessRights.contains(TO_STRING)) {
            throw new UnauthorizedException(userCtx, SUPER_PC_REP, TO_STRING);
        }

        return null;
    }

    @Override
    public void fromString(String s, PolicyDeserializer policyDeserializer) throws PMException {
        AccessRightSet accessRights = policyReviewer.getAccessRights(userCtx, SUPER_PC_REP);
        if (!accessRights.contains(FROM_STRING)) {
            throw new UnauthorizedException(userCtx, SUPER_PC_REP, FROM_STRING);
        }
    }
}
