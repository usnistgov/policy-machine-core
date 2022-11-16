package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.pap.store.PolicyStore;
import gov.nist.csd.pm.policy.events.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.events.CreatePolicyClassEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAccessRights;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

public class SuperPolicy {

    private SuperPolicy() { }

    public static final String SUPER_PREFIX = "super";

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_policy";
    public static final String SUPER_UA = "super_ua";
    public static final String SUPER_UA1 = "super_ua1";
    public static final String SUPER_PC_REP = pcRepObjectAttribute(SUPER_PC);
    public static final String SUPER_OA = "super_oa";

    public static String pcRepObjectAttribute(String policyClass) {
        return policyClass + "_pc_rep";
    }

    protected static void verifySuperPolicy(Graph graph) throws PMException {
        // start a transaction on the policy store the graph uses
        // then use Graph methods to take advantage of the policy emitter
        // internally, the Graph methods will be using a policy store in Tx mode
        runTx(graph.store(), () -> {

            if (!graph.nodeExists(SUPER_PC)) {
                graph.createPolicyClass(SUPER_PC);
            }

            if (!graph.nodeExists(SUPER_UA)) {
                graph.createUserAttribute(SUPER_UA, SUPER_PC);
            } else if (!graph.getParents(SUPER_UA).contains(SUPER_PC)) {
                graph.assign(SUPER_UA, SUPER_PC);
            }

            if (!graph.nodeExists(SUPER_UA1)) {
                graph.createUserAttribute(SUPER_UA1, SUPER_PC);
            } else if (!graph.getParents(SUPER_UA1).contains(SUPER_PC)) {
                graph.assign(SUPER_UA1, SUPER_PC);
            }

            if (!graph.nodeExists(SUPER_OA)) {
                graph.createObjectAttribute(SUPER_OA, SUPER_PC);
            } else if (!graph.getParents(SUPER_OA).contains(SUPER_PC)) {
                graph.assign(SUPER_OA, SUPER_PC);
            }

            if (!graph.nodeExists(SUPER_USER)) {
                graph.createUser(SUPER_USER, SUPER_UA, SUPER_UA1);
            }

            List<String> parents = graph.getParents(SUPER_USER);
            if (!parents.contains(SUPER_UA)) {
                graph.assign(SUPER_USER, SUPER_UA);
            } else if (!parents.contains(SUPER_UA1)) {
                graph.assign(SUPER_USER, SUPER_UA1);
            }

            if (!graph.nodeExists(SUPER_PC_REP)) {
                graph.createObjectAttribute(SUPER_PC_REP, SUPER_OA);
            } else if (!graph.getParents(SUPER_PC_REP).contains(SUPER_OA)) {
                graph.assign(SUPER_PC_REP, SUPER_OA);
            }

            // associate the super_ua1 and super_ua to provide * rights to the super user on itself
            // if the association already exists this will do nothing
            graph.associate(SUPER_UA1, SUPER_UA, allAccessRights());

            // associate super ua and super oa
            graph.associate(SUPER_UA, SUPER_OA, allAccessRights());

            // associate the super ua with each policy not super_pc
            for (String pc : graph.getPolicyClasses()) {
                if (pc.equals(SUPER_PC)) {
                    continue;
                }

                List<String> children = graph.getChildren(pc);
                for (String child : children) {
                    graph.associate(SUPER_UA, child, allAccessRights());
                }
            }
        });
    }

    protected static void assignedToPolicyClass(Graph graph, String child, String pc) throws PMException {
        runTx(graph.store(), () -> {
            if (child.startsWith(SUPER_PREFIX)) {
                return;
            }

            if (!graph.nodeExists(SUPER_UA)) {
                graph.createUserAttribute(SUPER_UA, SUPER_PC);
            }

            graph.associate(SUPER_UA, child, allAccessRights());
        });
    }

    protected static void createPolicyClass(Graph graph, String name, Map<String, String> properties) throws PMException {
        PolicyStore store = graph.store();
        runTx(store, () -> {
            // create pc node
            store.graph().createPolicyClass(name, properties);
            graph.emitEvent(new CreatePolicyClassEvent(name, properties));

            // create pc rep node in super policy
            if (!store.graph().nodeExists(SUPER_OA)) {
                graph.createObjectAttribute(SUPER_OA, SUPER_PC);
            }

            String pcRep = pcRepObjectAttribute(name);
            store.graph().createObjectAttribute(pcRep, noprops(), SUPER_OA);
            graph.emitEvent(new CreateObjectAttributeEvent(pcRep, noprops(), SUPER_OA));
        });
    }
}