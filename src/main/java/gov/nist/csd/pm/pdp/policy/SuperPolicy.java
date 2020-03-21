package gov.nist.csd.pm.pdp.policy;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;

import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ALL_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.NAMESPACE_PROPERTY;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

public class SuperPolicy {

    private static final Node superUser = new Node("super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"));
    private Node superUA1;
    private Node superUA2;
    private Node superPolicyClassRep;
    private Node superOA;
    private Node superO;
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
    public Node getSuperObject() {
        return superO;
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

        if (!graph.exists("super_pc_rep")) {
            superPolicyClassRep = graph.createNode(superPCRep, NodeType.OA,
                    Node.toProperties(NAMESPACE_PROPERTY, "super", "pc", String.valueOf(superPC.getName())), superOA.getName());
        }

        // check super ua1 is assigned to super pc
        Set<String> children = graph.getChildren(superPC.getName());
        if(!children.contains(superUA1.getName())) {
            graph.assign(superUA1.getName(), superPC.getName());
        }
        // check super ua2 is assigned to super pc
        if(!children.contains(superUA2.getName())) {
            graph.assign(superUA2.getName(), superPC.getName());
        }
        // check super ua2 is assigned to super pc
        children = graph.getChildren(superPC.getName());
        if(!children.contains(superUA2.getName())) {
            graph.assign(superUA2.getName(), superPC.getName());
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
            graph.assign(superOA.getName(), superPC.getName());
        }
        // check super o is assigned to super oa
        children = graph.getChildren(superOA.getName());
        if(!children.contains(superPolicyClassRep.getName())) {
            graph.assign(superPolicyClassRep.getName(), superOA.getName());
        }

        // associate super ua to super oa
        graph.associate(superUA1.getName(), superOA.getName(), new OperationSet(ALL_OPERATIONS));
        graph.associate(superUA2.getName(), superUA1.getName(), new OperationSet(ALL_OPERATIONS));
    }
}
