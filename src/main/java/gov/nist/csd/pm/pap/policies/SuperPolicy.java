package gov.nist.csd.pm.pap.policies;

import gov.nist.csd.pm.pap.naming.Naming;
import gov.nist.csd.pm.policy.author.GraphAuthor;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.ALL_ACCESS_RIGHTS_SET;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.*;

public class SuperPolicy {

    public static final String SUPER_PREFIX = "super";

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_pc";
    public static final String SUPER_UA = "super_ua";
    public static final String SUPER_OA = "super_oa";
    public static final String SUPER_OBJECT = "super_object";

    public static void configureSuperPolicy(GraphAuthor graph) throws PMException {
        String baseUA = Naming.baseUserAttribute(SUPER_PC);
        String baseOA = Naming.baseObjectAttribute(SUPER_PC);
        String repOA = Naming.pcRepObjectAttribute(SUPER_PC);

        // create super policy class node and base attributes
        if (!graph.nodeExists(SUPER_PC)) {
            graph.createPolicyClass(SUPER_PC, toProperties(REP_PROPERTY, repOA));
        }
        if (!graph.nodeExists(baseUA)) {
            graph.createUserAttribute(baseUA, noprops(), SUPER_PC);
        }
        if (!graph.nodeExists(baseOA)) {
            graph.createObjectAttribute(baseOA, noprops(), SUPER_PC);
        }
        if (!graph.nodeExists(repOA)) {
            graph.createObjectAttribute(repOA, noprops(), SUPER_PC);
        }

        // create super ua and user
        if (!graph.nodeExists(SUPER_UA)) {
            graph.createUserAttribute(SUPER_UA, noprops(), SUPER_PC);
        }
        if (!graph.nodeExists(SUPER_USER)) {
            graph.createUser(SUPER_USER, noprops(), SUPER_UA, baseUA);
        }

        // create super oa and object
        if (!graph.nodeExists(SUPER_OA)) {
            graph.createObjectAttribute(SUPER_OA, noprops(), baseOA);
        }

        if (!graph.nodeExists(SUPER_OBJECT)) {
            graph.createObject(SUPER_OBJECT, noprops(), SUPER_OA);
        }

        // associate the super ua with the super_base_ua and super_oa
        graph.associate(SUPER_UA, baseUA, ALL_ACCESS_RIGHTS_SET);
        graph.associate(SUPER_UA, SUPER_OA, ALL_ACCESS_RIGHTS_SET);
    }
}