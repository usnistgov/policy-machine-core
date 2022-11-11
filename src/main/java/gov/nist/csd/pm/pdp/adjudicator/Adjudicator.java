package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PolicyReviewer;
import gov.nist.csd.pm.policy.author.*;
import gov.nist.csd.pm.policy.author.pal.PALSerializable;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.Arrays;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperPolicy.SUPER_PC_REP;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.FROM_PAL;
import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.TO_PAL;

public class Adjudicator implements PolicyAuthor, PALSerializable {

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
    public String toPAL(boolean format) throws PMException {
        AccessRightSet accessRights = policyReviewer.getAccessRights(userCtx, SUPER_PC_REP);
        if (!accessRights.contains(TO_PAL)) {
            throw new UnauthorizedException(userCtx, SUPER_PC_REP, TO_PAL);
        }

        return null;
    }

    @Override
    public void fromPAL(UserContext author, String input, FunctionDefinitionStatement... customFunctions) throws PMException {
        AccessRightSet accessRights = policyReviewer.getAccessRights(userCtx, SUPER_PC_REP);
        if (!accessRights.contains(FROM_PAL)) {
            throw new UnauthorizedException(userCtx, SUPER_PC_REP, FROM_PAL);
        }
    }
}
