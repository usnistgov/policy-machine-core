package gov.nist.csd.pm.pap.policies;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.ALL_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.NAMESPACE_PROPERTY;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

public class SuperPolicy {

    public static final String SUPER_USER = "super";
    public static final String SUPER_PC = "super_pc";
    public static final String SUPER_PC_REP = "super_pc_rep";
    public static final String SUPER_UA1 = "super_ua1";
    public static final String SUPER_UA2 = "super_ua2";
    public static final String SUPER_OA = "super_oa";

    private final Node superUser = new Node("super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"));
    private Node superUA1;
    private Node superUA2;
    private Node superPolicyClassRep;
    private Node superOA;
    private Node superPC;

    public SuperPolicy() { }

    public Node getSuperUser() {
        return superUser;
    }

    public Node getSuperUserAttribute() {
        return superUA1;
    }

    public Node getSuperUserAttribute2() {
        return superUA2;
    }

    public Node getSuperPolicyClassRep() {
        return superPolicyClassRep;
    }

    public Node getSuperObjectAttribute() {
        return superOA;
    }

    public Node getSuperPolicyClass() {
        return superPC;
    }

    public void configure(Graph graph) throws PMException {
        String superPCRep = "super_pc_rep";
        if (!graph.exists("super_pc")) {
            Map<String, String> props = Node.toProperties(NAMESPACE_PROPERTY, "super", REP_PROPERTY, "super_pc_rep");
            superPC = graph.createPolicyClass("super_pc", props);
        } else {
            superPC = graph.getNode("super_pc");
            superPC.getProperties().put(REP_PROPERTY, superPCRep);
            graph.updateNode(superPC.getName(), superPC.getProperties());
        }

        if (!graph.exists("super_ua1")) {
            superUA1 = graph.createNode("super_ua1", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getName());
        } else {
            superUA1 = graph.getNode("super_ua1");
        }

        if (!graph.exists("super_ua2")) {
            superUA2 = graph.createNode("super_ua2", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getName());
        } else {
            superUA2 = graph.getNode("super_ua2");
        }

        if (!graph.exists("super")) {
            graph.createNode("super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"), superUA1.getName(), superUA2.getName());
        }

        if (!graph.exists("super_oa")) {
            superOA = graph.createNode("super_oa", OA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getName());
        } else {
            superOA = graph.getNode("super_oa");
        }

        if (!graph.exists(superPCRep)) {
            superPolicyClassRep = graph.createNode(superPCRep, NodeType.OA,
                    Node.toProperties(NAMESPACE_PROPERTY, "super", "pc", superPC.getName()), superOA.getName());
        } else {
            superPolicyClassRep = graph.getNode(superPCRep);
        }

        // check super ua1 is assigned to super pc
        Set<String> children = graph.getChildren(superPC.getName());
        if(!children.contains(superUA1.getName())) {
            if (!graph.getChildren("super_pc_default_UA").contains(superUA1.getName())) {
                graph.assign(superUA1.getName(), superPC.getName());
            }
        }

        // check super ua2 is assigned to super pc
        if(!children.contains(superUA2.getName())) {
            if (!graph.getChildren("super_pc_default_UA").contains(superUA2.getName())) {
                graph.assign(superUA2.getName(), superPC.getName());
            }
        }

        // check super user is assigned to super ua1
        children = graph.getChildren(superUA1.getName());
        if(!children.contains(superUser.getName())) {
            graph.assign(superUser.getName(), superUA1.getName());
        }

        // check super user is assigned to super ua2
        children = graph.getChildren(superUA2.getName());
        if(!children.contains(superUser.getName())) {
            graph.assign(superUser.getName(), superUA2.getName());
        }

        // check super oa is assigned to super pc
        children = graph.getChildren(superPC.getName());
        if(!children.contains(superOA.getName())) {
            if (!graph.getChildren("super_pc_default_OA").contains(superOA.getName())) {
                graph.assign(superOA.getName(), superPC.getName());
            }
        }

        // check super o is assigned to super oa
        children = graph.getChildren(superOA.getName());
        if(!children.contains(superPolicyClassRep.getName())) {
            graph.assign(superPolicyClassRep.getName(), superOA.getName());
        }

        // associate super_ua1 to super_oa and super_ua2 to super_ua1
        graph.associate(superUA1.getName(), superOA.getName(), new OperationSet(ALL_OPS));
        graph.associate(superUA2.getName(), superUA1.getName(), new OperationSet(ALL_OPS));
        graph.associate(superUA1.getName(), superUA2.getName(), new OperationSet(ALL_OPS));
        configurePolicyClasses(graph);
    }

    private void configurePolicyClasses(Graph graph) throws PMException {
        Set<String> policyClasses = graph.getPolicyClasses();
        for (String pc : policyClasses) {
            // configure default nodes
            String rep = pc + "_rep";
            String defaultUA = pc + "_default_UA";
            String defaultOA = pc + "_default_OA";

            if (!graph.exists(defaultOA)) {
                graph.createNode(defaultOA, OA, Node.toProperties(NAMESPACE_PROPERTY, pc), pc);
            }
            if (!graph.exists(defaultUA)) {
                graph.createNode(defaultUA, UA, Node.toProperties(NAMESPACE_PROPERTY, pc), pc);
            }
            // update pc node if necessary
            Node node = graph.getNode(pc);
            Map<String, String> props = node.getProperties();
            props.put("default_ua", defaultUA);
            props.put("default_oa", defaultOA);
            props.put(REP_PROPERTY, rep);
            graph.updateNode(pc, props);

            //remove potential parents of super uas
            if (graph.getParents(superUA1.getName()).contains("super_pc_default_UA")) {
                graph.deassign(superUA1.getName(), "super_pc_default_UA");
            }
            if (graph.getParents(superUA2.getName()).contains("super_pc_default_UA")) {
                graph.deassign(superUA2.getName(), "super_pc_default_UA");
            }
            // assign both super uas if not already
            if (!graph.isAssigned(superUA1.getName(), pc)) {
                System.out.println("new assign super_ua1 to pc");
                graph.assign(superUA1.getName(), pc);
            }
            if (!graph.isAssigned(superUA2.getName(), pc)) {
                graph.assign(superUA2.getName(), pc);
            }

            // associate super ua 1 with pc default nodes
            graph.associate(superUA1.getName(), defaultUA, new OperationSet(ALL_OPS));
            graph.associate(superUA1.getName(), defaultOA, new OperationSet(ALL_OPS));

            // create the rep
            if (!graph.exists(rep)) {
                graph.createNode(rep, OA, Node.toProperties("pc", pc), superOA.getName());
            } else {
                // check that the rep is assigned to the super OA
                if (!graph.isAssigned(rep, superOA.getName())) {
                    graph.assign(rep, superOA.getName());
                }
            }
        }
    }
}
