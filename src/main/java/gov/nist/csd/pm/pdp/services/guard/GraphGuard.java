package gov.nist.csd.pm.pdp.services.guard;

import gov.nist.csd.pm.exceptions.PMAuthorizationException;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.*;
import static gov.nist.csd.pm.operations.Operations.UPDATE_NODE;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

public class GraphGuard extends Guard {

    public GraphGuard(PAP pap, OperationSet resourceOps) {
        super(pap, resourceOps);
    }

    public void checkCreatePolicyClass(UserContext userCtx) throws PMException {
        // check that the user can create a policy class
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperPolicyClassRep().getName(), CREATE_POLICY_CLASS)) {
            throw new PMAuthorizationException("unauthorized permissions to create a policy class");
        }
    }

    public void checkCreateNode(UserContext userCtx, String initialParent, String[] additionalParents) throws PMException {
        // check that the user has the permission to assign to the parent node
        if (!hasPermissions(userCtx, initialParent, ASSIGN_TO)) {
            // if the user cannot assign to the parent node, delete the newly created node
            throw new PMAuthorizationException(String.format("unauthorized permission \"%s\" on node %s", ASSIGN_TO, initialParent));
        }

        // check any additional parents before assigning
        for (String parent : additionalParents) {
            if (!hasPermissions(userCtx, parent, ASSIGN_TO)) {
                // if the user cannot assign to the parent node, delete the newly created node
                throw new PMAuthorizationException(String.format("unauthorized permission \"%s\" on %s", ASSIGN_TO, parent));
            }
        }
    }

    public void checkUpdateNode(UserContext userCtx, String name) throws PMException {
        // check that the user can update the node
        if(!hasPermissions(userCtx, name, UPDATE_NODE)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", UPDATE_NODE, name));
        }
    }

    public void checkDeleteNode(UserContext userCtx, String node) throws PMException {
        // check the user can deassign the node
        if (!hasPermissions(userCtx, node, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", node, DEASSIGN));
        }

        // check that the user can deassign from the node's parents
        Set<String> parents = pap.getGraphAdmin().getParents(node);
        for(String parent : parents) {
            if(!hasPermissions(userCtx, parent, DEASSIGN_FROM)) {
                throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parent, DEASSIGN_FROM));
            }
        }
    }

    public boolean checkExists(UserContext userCtx, String name) throws PMException {
        // a user only needs one permission to know a node exists
        // however receiving an unauthorized exception would let the user know it exists
        // therefore, false is returned if they don't have permissions on the node
        return hasPermissions(userCtx, name);
    }

    public void filter(UserContext userCtx, Set<String> nodes) throws PMException {
        nodes.removeIf(node -> {
            try {
                return !hasPermissions(userCtx, node);
            } catch (PMException e) {
                return true;
            }
        });
    }

    public void filterNodes(UserContext userCtx, Set<Node> nodes) {
        nodes.removeIf(node -> {
            try {
                return !hasPermissions(userCtx, node.getName());
            } catch (PMException e) {
                return true;
            }
        });
    }

    public void filter(UserContext userCtx, Map<String, OperationSet> map) {
        map.keySet().removeIf(key -> {
            try {
                return !hasPermissions(userCtx, key);
            } catch (PMException e) {
                return true;
            }
        });
    }

    public void checkAssign(UserContext userCtx, String child, String parent) throws PMException {
        //check the user can assign the child
        if(!hasPermissions(userCtx, child, ASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", ASSIGN, child));
        }

        // check that the user can assign to the parent node
        if (!hasPermissions(userCtx, parent, ASSIGN_TO)) {
            throw new PMAuthorizationException(String.format("unauthorized permission %s on node %s", ASSIGN_TO, parent));
        }
    }

    public void checkDeassign(UserContext userCtx, String child, String parent) throws PMException {
        //check the user can deassign the child
        if(!hasPermissions(userCtx, child, DEASSIGN)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", child, DEASSIGN));
        }

        //check that the user can deassign from the parent
        if (!hasPermissions(userCtx, parent, DEASSIGN_FROM)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", parent, DEASSIGN_FROM));
        }
    }

    public void checkAssociate(UserContext userCtx, String ua, String target) throws PMException {
        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, ua, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", ua, ASSOCIATE));
        }
        if (!hasPermissions(userCtx, target, ASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", target, ASSOCIATE));
        }
    }

    public void checkDissociate(UserContext userCtx, String ua, String target) throws PMException {
        //check the user can associate the source and target nodes
        if(!hasPermissions(userCtx, ua, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", ua, DISASSOCIATE));
        }
        if (!hasPermissions(userCtx, target, DISASSOCIATE)) {
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", target, DISASSOCIATE));
        }
    }

    public void checkGetAssociations(UserContext userCtx, String node) throws PMException {
        //check the user can get the associations of the source node
        if(!hasPermissions(userCtx, node, GET_ASSOCIATIONS)){
            throw new PMAuthorizationException(String.format("unauthorized permissions on %s: %s", node, GET_ASSOCIATIONS));
        }
    }

    public void checkToJson(UserContext userCtx) throws PMException {
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperPolicyClassRep().getName(), TO_JSON)) {
            throw new PMAuthorizationException("unauthorized permissions to serialize graph to json");
        }
    }

    public void checkFromJson(UserContext userCtx) throws PMException {
        if (!hasPermissions(userCtx, pap.getGraphAdmin().getSuperPolicy().getSuperPolicyClassRep().getName(), FROM_JSON)) {
            throw new PMAuthorizationException("unauthorized permissions to deserialize json to graph");
        }
    }
}
