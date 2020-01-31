package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;

import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ALL_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.U;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.NAMESPACE_PROPERTY;

public class SuperPolicy {

    private static final int SUPER_ID = 0;
    private static final Node superUser = new Node(SUPER_ID, "super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"));
    private Node superUA;
    private Node superO;
    private Node superOA;

    public SuperPolicy() { }

    public Node getSuperUser() {
        return superUser;
    }

    public Node getSuperUserAttribute() {
        return superUA;
    }

    public Node getSuperObject() {
        return superO;
    }

    public Node getSuperObjectAttribute() {
        return superOA;
    }

    public void configure(Graph graph) throws PMException {
        Random rand = new Random();

        Map<String, String> filter = Node.toProperties(NAMESPACE_PROPERTY, "super");

        Set<Node> nodes = graph.search("super", NodeType.PC.toString(), filter);
        Node superPC;
        if(nodes.isEmpty()) {
            // add the rep oa ID to the properties
            Map<String, String> props = Node.toProperties(NAMESPACE_PROPERTY, "super");
            superPC = graph.createPolicyClass(rand.nextLong(), "super", props);
        } else {
            superPC = nodes.iterator().next();
        }

        nodes = graph.search("super_ua1", UA.toString(), filter);
        if(nodes.isEmpty()) {
            superUA = graph.createNode(rand.nextLong(), "super_ua1", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superUA = nodes.iterator().next();
        }
        nodes = graph.search("super_ua2", UA.toString(), filter);
        Node superUA2;
        if(nodes.isEmpty()) {
            superUA2 = graph.createNode(rand.nextLong(), "super_ua2", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superUA2 = nodes.iterator().next();
        }
        nodes = graph.search("super", U.toString(), filter);
        if(nodes.isEmpty()) {
            graph.createNode(0, "super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"), superUA.getID(), superUA2.getID());
        }

        nodes = graph.search("super", NodeType.OA.toString(), filter);
        if(nodes.isEmpty()) {
            superOA = graph.createNode(rand.nextLong(), "super", NodeType.OA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superOA = nodes.iterator().next();
        }
        nodes = graph.search("super", NodeType.O.toString(), filter);
        if(nodes.isEmpty()) {
            superO = graph.createNode(rand.nextLong(), "super", NodeType.O, Node.toProperties(NAMESPACE_PROPERTY, "super"), superOA.getID());
        } else {
            superO = nodes.iterator().next();
        }

        // check super ua1 is assigned to super pc
        Set<Long> children = graph.getChildren(superPC.getID());
        if(!children.contains(superUA.getID())) {
            graph.assign(superUA.getID(), superPC.getID());
        }
        // check super ua2 is assigned to super pc
        children = graph.getChildren(superPC.getID());
        if(!children.contains(superUA2.getID())) {
            graph.assign(superUA2.getID(), superPC.getID());
        }
        // check super user is assigned to super ua1
        children = graph.getChildren(superUA.getID());
        if(!children.contains(superUser.getID())) {
            graph.assign(superUser.getID(), superUA.getID());
        }
        // check super user is assigned to super ua2
        children = graph.getChildren(superUA2.getID());
        if(!children.contains(superUser.getID())) {
            graph.assign(superUser.getID(), superUA2.getID());
        }
        // check super oa is assigned to super pc
        children = graph.getChildren(superPC.getID());
        if(!children.contains(superOA.getID())) {
            graph.assign(superOA.getID(), superPC.getID());
        }
        // check super o is assigned to super oa
        children = graph.getChildren(superOA.getID());
        if(!children.contains(superO.getID())) {
            graph.assign(superO.getID(), superOA.getID());
        }

        // associate super ua to super oa
        graph.associate(superUA.getID(), superOA.getID(), new OperationSet(ALL_OPERATIONS));
        graph.associate(superUA.getID(), superUA2.getID(), new OperationSet(ALL_OPERATIONS));
    }
}
