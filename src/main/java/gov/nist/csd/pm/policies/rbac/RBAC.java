package gov.nist.csd.pm.policies.rbac;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

/**
 * Utilities for any operation relating to the RBAC NGAC concept
 */
public class RBAC {
    /**
     * This sets the RBAC PC for any of the methods in this class.
     * If the given PC already exists it will mark it as the RBAC PC,
     * otherwise it will create and mark it.
     *
     * This will likely be the first call in any method of this class.
     *
     * @param RBACname the name of the RBAC PC
     * @param graph
     * @return the RBAC PC
     * @throws PMException
     */
    private static Node configure (String RBACname, Graph graph) throws PMException {
        if (!graph.exists(RBACname)) {
            return graph.createPolicyClass(RBACname, Node.toProperties("type", "RBAC"));
        } else {
            Node RBAC = graph.getNode(RBACname);

            // add ngac_type=RBAC to properties
            String typeValue = RBAC.getProperties().get("ngac_type");
            if (typeValue == null) {
                RBAC.addProperty("ngac_type", "RBAC");
            } else if (!typeValue.equals("RBAC")) {
                throw new PMException("Node cannot have property key of ngac_type");
            }

            return RBAC;
        }
    }

    // TODO:
        // assign role (user --> role, role --> role)
        // remove role (user -x> role, role -x> role)
        // get roles
        // give role permissions on an OA/O
}
