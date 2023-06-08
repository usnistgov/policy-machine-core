package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.events.graph.AssignEvent;
import gov.nist.csd.pm.policy.events.graph.AssociateEvent;
import gov.nist.csd.pm.policy.events.graph.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.events.graph.CreatePolicyClassEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.allAccessRights;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
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

    public static boolean isSuperPolicyNode(String node) {
        return node.equals(SUPER_USER) ||
               node.equals(SUPER_PC) ||
               node.equals(SUPER_UA) ||
               node.equals(SUPER_UA1) ||
               node.equals(SUPER_OA) ||
               node.equals(SUPER_PC_REP);
    }

    protected static void verifySuperPolicy(PolicyStore store) throws PMException {
        // start a transaction on the policy store the graph uses
        // then use Graph methods to take advantage of the policy emitter
        // internally, the Graph methods will be using a policy store in Tx mode
        runTx(store, () -> {
            if (!store.graph().nodeExists(SUPER_PC)) {
                store.graph().createPolicyClass(SUPER_PC);
            }

            if (!store.graph().nodeExists(SUPER_UA)) {
                store.graph().createUserAttribute(SUPER_UA, SUPER_PC);
            } else if (!store.graph().getParents(SUPER_UA).contains(SUPER_PC)) {
                store.graph().assign(SUPER_UA, SUPER_PC);
            }

            if (!store.graph().nodeExists(SUPER_UA1)) {
                store.graph().createUserAttribute(SUPER_UA1, SUPER_PC);
            } else if (!store.graph().getParents(SUPER_UA1).contains(SUPER_PC)) {
                store.graph().assign(SUPER_UA1, SUPER_PC);
            }

            if (!store.graph().nodeExists(SUPER_OA)) {
                store.graph().createObjectAttribute(SUPER_OA, SUPER_PC);
            } else if (!store.graph().getParents(SUPER_OA).contains(SUPER_PC)) {
                store.graph().assign(SUPER_OA, SUPER_PC);
            }

            if (!store.graph().nodeExists(SUPER_USER)) {
                store.graph().createUser(SUPER_USER, SUPER_UA, SUPER_UA1);
            }

            List<String> parents = store.graph().getParents(SUPER_USER);
            if (!parents.contains(SUPER_UA)) {
                store.graph().assign(SUPER_USER, SUPER_UA);
            } else if (!parents.contains(SUPER_UA1)) {
                store.graph().assign(SUPER_USER, SUPER_UA1);
            }

            if (!store.graph().nodeExists(SUPER_PC_REP)) {
                store.graph().createObjectAttribute(SUPER_PC_REP, SUPER_OA);
            } else if (!store.graph().getParents(SUPER_PC_REP).contains(SUPER_OA)) {
                store.graph().assign(SUPER_PC_REP, SUPER_OA);
            }

            // associate the super_ua1 and super_ua to provide * rights to the super user on itself
            // if the association already exists this will do nothing
            store.graph().associate(SUPER_UA1, SUPER_UA, allAccessRights());

            // associate super ua and super oa
            store.graph().associate(SUPER_UA, SUPER_OA, allAccessRights());

            // associate the super ua with each policy not super_pc
            for (String pc : store.graph().getPolicyClasses()) {
                if (pc.equals(SUPER_PC)) {
                    continue;
                }

                String repOAName = pcRepObjectAttribute(pc);

                if (!store.graph().nodeExists(repOAName)) {
                    store.graph().createObjectAttribute(repOAName, SUPER_OA);
                }

                List<String> children = store.graph().getChildren(pc);
                for (String child : children) {
                    store.graph().associate(SUPER_UA, child, allAccessRights());
                }
            }
        });
    }

    protected static List<PolicyEvent> assignedToPolicyClass(PolicyStore store, String child, String pc) throws PMException {
        List<PolicyEvent> events = new ArrayList<>();

        runTx(store, () -> {
            if (child.startsWith(SUPER_PREFIX)) {
                return;
            }

            if (!store.graph().nodeExists(SUPER_UA)) {
                store.graph().createUserAttribute(SUPER_UA, SUPER_PC);
                events.add(new AssignEvent(SUPER_UA, SUPER_PC));
            }

            store.graph().associate(SUPER_UA, child, allAccessRights());
            events.add(new AssociateEvent(SUPER_UA, child, allAccessRights()));
        });

        return events;
    }

    protected static List<PolicyEvent> createPolicyClass(PolicyStore store, String name, Map<String, String> properties) throws PMException {
        List<PolicyEvent> events = new ArrayList<>();

        runTx(store, () -> {
            // create pc node
            store.graph().createPolicyClass(name, properties);
            events.add(new CreatePolicyClassEvent(name, properties));

            // create pc rep node in super policy
            if (!store.graph().nodeExists(SUPER_OA)) {
                store.graph().createObjectAttribute(SUPER_OA, SUPER_PC);
            }

            String pcRep = pcRepObjectAttribute(name);
            if (store.graph().nodeExists(pcRep)) {
                return;
            }

            store.graph().createObjectAttribute(pcRep, NO_PROPERTIES, SUPER_OA);
            events.add(new CreateObjectAttributeEvent(pcRep, NO_PROPERTIES, SUPER_OA));
        });

        return events;
    }
}