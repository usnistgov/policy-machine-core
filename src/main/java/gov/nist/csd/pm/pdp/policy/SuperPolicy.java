package gov.nist.csd.pm.pdp.policy;

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
    private Node superUA2;
    private Node superPolicyClassRep;
    private Node superOA;
    private Node superPC;

    public SuperPolicy() { }

    public Node getSuperUser() {
        return superUser;
    }

    public Node getSuperUserAttribute() {
        return superUA;
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
        Random rand = new Random();

        Map<String, String> filter = Node.toProperties(NAMESPACE_PROPERTY, "super");

        Set<Node> nodes = graph.search("super_pc", NodeType.PC, filter);
        Node superPC;
        long superPCRepID = rand.nextLong();
        if(nodes.isEmpty()) {
            Map<String, String> props = Node.toProperties(NAMESPACE_PROPERTY, "super", "rep_id", String.valueOf(superPCRepID));
            superPC = graph.createPolicyClass(rand.nextLong(), "super_pc", props);
        } else {
            superPC = nodes.iterator().next();
            if (superPC.getProperties().containsKey("rep_id")) {
                superPCRepID = Long.parseLong(superPC.getProperties().get("rep_id"));
            } else {
                superPC.getProperties().put("rep_id", String.valueOf(superPCRepID));
                graph.updateNode(superPC.getID(), null, superPC.getProperties());
            }
        }

        nodes = graph.search("super_ua1", UA, filter);
        if(nodes.isEmpty()) {
            superUA = graph.createNode(rand.nextLong(), "super_ua1", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superUA = nodes.iterator().next();
        }
        nodes = graph.search("super_ua2", UA, filter);
        if(nodes.isEmpty()) {
            superUA2 = graph.createNode(rand.nextLong(), "super_ua2", UA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superUA2 = nodes.iterator().next();
        }
        nodes = graph.search("super_u", U, filter);
        if(nodes.isEmpty()) {
            graph.createNode(0, "super", U, Node.toProperties(NAMESPACE_PROPERTY, "super"), superUA.getID(), superUA2.getID());
        }

        nodes = graph.search("super_oa", NodeType.OA, filter);
        if(nodes.isEmpty()) {
            superOA = graph.createNode(rand.nextLong(), "super_oa", NodeType.OA, Node.toProperties(NAMESPACE_PROPERTY, "super"), superPC.getID());
        } else {
            superOA = nodes.iterator().next();
        }
        nodes = graph.search("super rep", NodeType.OA, filter);
        if(nodes.isEmpty()) {
            superPolicyClassRep = graph.createNode(superPCRepID, "super rep", NodeType.OA,
                    Node.toProperties(NAMESPACE_PROPERTY, "super", "pc", String.valueOf(superPC.getID())), superOA.getID());
        } else {
            superPolicyClassRep = nodes.iterator().next();
            if (!superPC.getProperties().containsKey("pc")) {
                superPC.getProperties().put("pc", String.valueOf(superPC.getID()));
                graph.updateNode(superPolicyClassRep.getID(), null, superPolicyClassRep.getProperties());
            }
        }

        // check super ua1 is assigned to super pc
        Set<Long> children = graph.getChildren(superPC.getID());
        if(!children.contains(superUA.getID())) {
            graph.assign(superUA.getID(), superPC.getID());
        }
        // check super ua2 is assigned to super pc
        if(!children.contains(superUA2.getID())) {
            graph.assign(superUA2.getID(), superPC.getID());
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
        if(!children.contains(superPolicyClassRep.getID())) {
            graph.assign(superPolicyClassRep.getID(), superOA.getID());
        }

        // associate super ua to super oa
        graph.associate(superUA.getID(), superOA.getID(), new OperationSet(ALL_OPERATIONS));
        graph.associate(superUA2.getID(), superUA.getID(), new OperationSet(ALL_OPERATIONS));
    }
}
