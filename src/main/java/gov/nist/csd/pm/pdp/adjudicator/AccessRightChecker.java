package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.RepObjectNotSetException;
import gov.nist.csd.pm.policy.exceptions.UnauthorizedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

import java.util.Arrays;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.REP_PROPERTY;

public class AccessRightChecker {

    private PAP pap;
    private PolicyReviewer policyReviewer;

    public AccessRightChecker(PAP pap, PolicyReviewer policyReviewer) {
        this.pap = pap;
        this.policyReviewer = policyReviewer;
    }

    public void check(UserContext userCtx, String target, String... toCheck) throws PMException {
        // if checking the permissions on a PC, check the permissions on the rep node for the PC
        Node targetNode = pap.graph().getNode(target);

        if (targetNode.getType().equals(PC)) {
            if (!targetNode.getProperties().containsKey(REP_PROPERTY)) {
                throw new RepObjectNotSetException(targetNode.getName());
            }

            target = targetNode.getProperties().get(REP_PROPERTY);
        }

        AccessRightSet accessRights = policyReviewer.getAccessRights(userCtx, target);
        if(accessRights.isEmpty()) {
            throw new UnauthorizedException(userCtx, target, toCheck);
        } else {
            if (!accessRights.containsAll(Arrays.asList(toCheck))) {
                throw new UnauthorizedException(userCtx, target, toCheck);
            }
        }
    }

}
