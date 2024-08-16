package gov.nist.csd.pm.pap.op;

import gov.nist.csd.pm.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.pattern.Pattern;
import gov.nist.csd.pm.pap.pml.pattern.ReferencedNodes;
import gov.nist.csd.pm.pdp.exception.UnauthorizedException;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.graph.node.Node;

import java.util.Arrays;
import java.util.Collection;

import static gov.nist.csd.pm.pap.graph.node.NodeType.PC;

public class PrivilegeChecker {

    public static void check(PAP pap, UserContext userCtx, String target, Collection<String> toCheck) throws PMException {
        // if checking the permissions on a PC, check the permissions on the target node for the PC
        Node targetNode = pap.query().graph().getNode(target);

        if (targetNode.getType().equals(PC)) {
            target = AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName();
        }

        AccessRightSet accessRights = pap.query().access().computePrivileges(userCtx, target);
        if (!accessRights.containsAll(toCheck)) {
            throw new UnauthorizedException(userCtx, target, toCheck.toArray(String[]::new));
        }
    }

    public static void check(PAP pap, UserContext userCtx, String target, String... toCheck) throws PMException {
        // if checking the permissions on a PC, check the permissions on the target node for the PC
        Node targetNode = pap.query().graph().getNode(target);

        if (targetNode.getType().equals(PC)) {
            target = AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName();
        }

        AccessRightSet accessRights = pap.query().access().computePrivileges(userCtx, target);
        if (!accessRights.containsAll(Arrays.asList(toCheck))) {
            throw new UnauthorizedException(userCtx, target, toCheck);
        }
    }

    public static void check(PAP pap, UserContext userCtx, Collection<String> targets, String... toCheck) throws PMException {
        for (String target : targets) {
            check(pap, userCtx, target, toCheck);
        }
    }

    public static void checkPattern(PAP pap, UserContext userCtx, Pattern pattern, String toCheck) throws PMException {
        ReferencedNodes referencedNodes = pattern.getReferencedNodes();
        if (referencedNodes.isAny()) {
            PrivilegeChecker.check(pap, userCtx, AdminPolicyNode.ADMIN_POLICY_OBJECT.nodeName(), toCheck);

            return;
        }

        for (String entity : referencedNodes.nodes()) {
            PrivilegeChecker.check(pap, userCtx, entity, toCheck);
        }

    }
}
