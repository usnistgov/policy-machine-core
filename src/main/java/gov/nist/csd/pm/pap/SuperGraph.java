package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.*;

import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ALL_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.NAMESPACE_PROPERTY;

public class SuperGraph {

    private static Node superPC, superUA1, superUA2, superU, superOA, superO;

    public static void check(Graph graph) throws PMException {
        Random rand = new Random();

        Map<String, String> filter = Node.toProperties(NAMESPACE_PROPERTY, "super");

        Set<Node> nodes = graph.search("super_ua1", UA.toString(), filter);
        if(nodes.isEmpty()) {
            superUA1 = graph.createNode(rand.nextLong(), "super_ua1", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"));
        } else {
            superUA1 = nodes.iterator().next();
        }
        nodes = graph.search("super_ua2", UA.toString(), filter);
        if(nodes.isEmpty()) {
            superUA2 = graph.createNode(rand.nextLong(), "super_ua2", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"));
        } else {
            superUA2 = nodes.iterator().next();
        }
        nodes = graph.search("super", NodeType.U.toString(), filter);
        if(nodes.isEmpty()) {
            superU = graph.createNode(rand.nextLong(), "super", NodeType.U, Node.toProperties(NAMESPACE_PROPERTY, "super"));
        } else {
            superU = nodes.iterator().next();
        }

        nodes = graph.search("super", NodeType.OA.toString(), filter);
        if(nodes.isEmpty()) {
            superOA = graph.createNode(rand.nextLong(), "super", NodeType.OA, Node.toProperties(NAMESPACE_PROPERTY, "super"));
        } else {
            superOA = nodes.iterator().next();
        }
        nodes = graph.search("super", NodeType.O.toString(), filter);
        if(nodes.isEmpty()) {
            superO = graph.createNode(rand.nextLong(), "super", NodeType.O, Node.toProperties(NAMESPACE_PROPERTY, "super"));
        } else {
            superO = nodes.iterator().next();
        }

        nodes = graph.search("super", NodeType.PC.toString(), filter);
        if(nodes.isEmpty()) {
            // add the rep oa ID to the properties
            Map<String, String> props = Node.toProperties(NAMESPACE_PROPERTY, "super");
            superPC = graph.createNode(rand.nextLong(), "super", NodeType.PC, props);
        } else {
            superPC = nodes.iterator().next();
        }

        // check super ua1 is assigned to super pc
        Set<Long> children = graph.getChildren(superPC.getID());
        if(!children.contains(superUA1.getID())) {
            graph.assign(superUA1.getID(), superPC.getID());
        }
        // check super ua2 is assigned to super pc
        children = graph.getChildren(superPC.getID());
        if(!children.contains(superUA2.getID())) {
            graph.assign(superUA2.getID(), superPC.getID());
        }
        // check super user is assigned to super ua1
        children = graph.getChildren(superUA1.getID());
        if(!children.contains(superU.getID())) {
            graph.assign(superU.getID(), superUA1.getID());
        }
        // check super user is assigned to super ua2
        children = graph.getChildren(superUA2.getID());
        if(!children.contains(superU.getID())) {
            graph.assign(superU.getID(),superUA2.getID());
        }
        // check super oa is assigned to super pc
        children = graph.getChildren(superPC.getID());
        if(!children.contains(superOA.getID())) {
            graph.assign(superOA.getID(), superPC.getID());
        }
        // check super o is assigned to super oa
        if(!children.contains(superO.getID())) {
            graph.assign(superO.getID(), superOA.getID());
        }

        // associate super ua to super oa
        graph.associate(superUA1.getID(), superOA.getID(), new OperationSet(ALL_OPERATIONS));
        graph.associate(superUA1.getID(), superUA2.getID(), new OperationSet(ALL_OPERATIONS));
    }

    public static Node getSuperPC() {
        return superPC;
    }

    public static Node getSuperUA1() {
        return superUA1;
    }

    public static Node getSuperUA2() {
        return superUA2;
    }

    public static Node getSuperU() {
        return superU;
    }

    public static Node getSuperOA() {
        return superOA;
    }

    public static Node getSuperO() {
        return superO;
    }
}
