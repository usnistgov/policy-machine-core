package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.GraphStore;
import gov.nist.csd.pm.pap.memory.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.exceptions.*;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssignmentException;
import gov.nist.csd.pm.policy.model.graph.relationships.InvalidAssociationException;
import gov.nist.csd.pm.policy.Graph;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.tx.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.WILDCARD;

class MemoryGraph extends MemoryStore<TxGraph> implements GraphStore, Transactional, BaseMemoryTx {

    private Map<String, Vertex> graph;
    private AccessRightSet resourceAccessRights;
    private List<String> pcs;
    private List<String> oas;
    private List<String> uas;
    private List<String> os;
    private List<String> us;

    protected MemoryTx<TxGraph> tx;

    private MemoryProhibitions memoryProhibitions;
    private MemoryObligations memoryObligations;

    public MemoryGraph() {
        initGraph();
    }

    public MemoryGraph(Graph graph) throws PMException {
        initGraph();
        buildFromGraph(graph);
    }

    private void initGraph() {
        graph = new HashMap<>();
        pcs = new ArrayList<>();
        oas = new ArrayList<>();
        uas = new ArrayList<>();
        os = new ArrayList<>();
        us = new ArrayList<>();
        resourceAccessRights = new AccessRightSet();
    }

    @Override
    public void beginTx() throws PMException {
        if (tx == null) {
            tx = new MemoryTx<>(false, 0, new TxGraph(new TxPolicyEventTracker(), this));
        }
        tx.beginTx();
    }

    @Override
    public void commit() throws PMException {
        tx.commit();
    }

    @Override
    public void rollback() throws PMException {
        tx.getStore().rollback();

        tx.rollback();
    }

    public void setMemoryProhibitions(MemoryProhibitions memoryProhibitions) {
        this.memoryProhibitions = memoryProhibitions;
    }

    public void setMemoryObligations(MemoryObligations memoryObligations) {
        this.memoryObligations = memoryObligations;
    }

    public void clear() {
        graph.clear();
        resourceAccessRights.clear();
        pcs.clear();
        oas.clear();
        uas.clear();
        us.clear();
        os.clear();
    }

    @Override
    public void setResourceAccessRights(AccessRightSet accessRightSet)
    throws AdminAccessRightExistsException, PMBackendException {
        checkSetResourceAccessRightsInput(accessRightSet);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.setResourceAccessRights(accessRightSet));

        resourceAccessRights.clear();
        resourceAccessRights.addAll(accessRightSet);
    }

    @Override
    public AccessRightSet getResourceAccessRights() {
        return new AccessRightSet(resourceAccessRights);
    }

    @Override
    public String createPolicyClass(String name, Map<String, String> properties)
    throws NodeNameExistsException, PMBackendException {
        checkCreatePolicyClassInput(name);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.createPolicyClass(name, properties));

        // create pc node
        createNodeInternal(name, PC, properties);

        // create pc rep oa
        String oa = AdminPolicy.policyClassObjectAttributeName(name);
        createNodeInternal(oa, OA, properties);

        // assign the oa to the pc
        assignInternal(oa, name);

        return name;
    }

    @Override
    public String createPolicyClass(String name) throws NodeNameExistsException, PMBackendException {
        return createPolicyClass(name, NO_PROPERTIES);
    }

    @Override
    public String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.createUserAttribute(name, properties, parent, parents));

        return createNode(name, UA, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        return createUserAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.createObjectAttribute(name, properties, parent, parents));

        return createNode(name, OA, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        return createObjectAttribute(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createObject(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.createObject(name, properties, parent, parents));

        return createNode(name, O, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        return createObject(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public String createUser(String name, Map<String, String> properties, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.createUser(name, properties, parent, parents));

        return createNode(name, U, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents)
    throws NodeDoesNotExistException, NodeNameExistsException, PMBackendException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        return createUser(name, NO_PROPERTIES, parent, parents);
    }

    @Override
    public void setNodeProperties(String name, Map<String, String> properties)
    throws NodeDoesNotExistException, PMBackendException {
        checkSetNodePropertiesInput(name);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.setNodeProperties(name, properties));

        graph.get(name).setProperties(properties);
    }

    @Override
    public boolean nodeExists(String name) {
        return graph.containsKey(name);
    }

    @Override
    public Node getNode(String name) throws NodeDoesNotExistException, PMBackendException {
        checkGetNodeInput(name);

        return new Node(graph.get(name).getNode());
    }

    @Override
    public List<String> search(NodeType type, Map<String, String> properties) {
        List<String> nodes = filterByType(type);
        return filterByProperties(nodes, properties);
    }

    @Override
    public List<String> getPolicyClasses() {
        return new ArrayList<>(pcs);
    }

    @Override
    public void deleteNode(String name)
    throws NodeHasChildrenException, NodeReferencedInProhibitionException, NodeReferencedInObligationException,
           PMBackendException {
        if (!checkDeleteNodeInput(name, memoryProhibitions, memoryObligations)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.deleteNode(name));

        NodeType type = graph.get(name).getNode().getType();

        runInternalTx(() -> {
            if (type == PC) {
                String rep = AdminPolicy.policyClassObjectAttributeName(name);
                deleteNode(rep);
            }

            deleteInternal(name);
        });
    }

    @Override
    public void assign(String child, String parent)
    throws PMBackendException, NodeDoesNotExistException, InvalidAssignmentException, AssignmentCausesLoopException {
        if (!checkAssignInput(child, parent)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.assign(child, parent));

        assignInternal(child, parent);
    }

    @Override
    public void deassign(String child, String parent) throws PMBackendException, NodeDoesNotExistException {
        if (!checkDeassignInput(child, parent)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.deassign(child, parent));

        deassignInternal(child, parent);
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMBackendException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.assignAll(children, target));

        runInternalTx(() -> {
            for (String c : children) {
                assign(c, target);
            }
        });
    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMBackendException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.deassignAll(children, target));

        runInternalTx(() -> {
            for (String c : children) {
                deassign(c, target);
            }
        });
    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMBackendException {
        // log the command if in a tx
        handleTxIfActive(tx -> tx.deassignAllFromAndDelete(target));

        runInternalTx(() -> {
            for (String c : getChildren(target)) {
                deassign(c, target);
            }

            deleteNode(target);
        });
    }

    @Override
    public List<String> getParents(String node) throws NodeDoesNotExistException, PMBackendException {
        checkGetParentsInput(node);

        return new ArrayList<>(graph.get(node).getParents());
    }


    @Override
    public List<String> getChildren(String node) throws NodeDoesNotExistException, PMBackendException {
        checkGetChildrenInput(node);

        return new ArrayList<>(graph.get(node).getChildren());
    }


    @Override
    public void associate(String ua, String target, AccessRightSet accessRights)
    throws InvalidAssociationException, UnknownAccessRightException, NodeDoesNotExistException, PMBackendException {
        checkAssociateInput(ua, target, accessRights);

        // log the command if in a tx
        handleTxIfActive(tx -> tx.associate(ua, target, accessRights));

        if (containsEdge(ua, target)) {
            // remove the existing association edge in order to update it
            dissociateInternal(ua, target);
        }

        associateInternal(ua, target, accessRights);
    }

    @Override
    public void dissociate(String ua, String target) throws NodeDoesNotExistException, PMBackendException {
        if (!checkDissociateInput(ua, target)) {
            return;
        }

        // log the command if in a tx
        handleTxIfActive(tx -> tx.dissociate(ua, target));

        dissociateInternal(ua, target);
    }

    @Override
    public List<Association> getAssociationsWithSource(String ua) throws NodeDoesNotExistException, PMBackendException {
        checkGetAssociationsWithSourceInput(ua);

        return new ArrayList<>(graph.get(ua).getOutgoingAssociations());
    }

    @Override
    public List<Association> getAssociationsWithTarget(String target)
    throws NodeDoesNotExistException, PMBackendException {
        checkGetAssociationsWithTargetInput(target);

        return new ArrayList<>(graph.get(target).getIncomingAssociations());
    }

    private void buildFromGraph(Graph graph) throws PMException {
        List<String> nodes = graph.search(ANY, NO_PROPERTIES);

        // add nodes to graph
        List<String> uas = new ArrayList<>();
        for (String n : nodes) {
            Node node = graph.getNode(n);
            createNodeInternal(n, node.getType(), node.getProperties());

            if (node.getType() == UA) {
                uas.add(n);
            }
        }

        // add assignments to graph
        for (String n : nodes) {
            List<String> parents = graph.getParents(n);
            for (String p : parents) {
                assignInternal(n, p);
            }
        }

        // add associations to graph
        for (String ua : uas) {
            List<Association> assocs = graph.getAssociationsWithSource(ua);
            for (Association a : assocs) {
                associate(ua, a.getTarget(), a.getAccessRightSet());
            }
        }
    }

    private String createNode(String name, NodeType type, Map<String, String> properties, String parent,
                              String... additionalParents)
    throws NodeNameExistsException, PMBackendException, NodeDoesNotExistException, InvalidAssignmentException,
           AssignmentCausesLoopException {
        checkCreateNodeInput(name, type, parent, additionalParents);

        createNodeInternal(name, type, properties);

        runInternalTx(() -> {
            assignInternal(name, parent);

            for (String additionalParent : additionalParents) {
                assignInternal(name, additionalParent);
            }
        });

        return name;
    }

    protected void createNodeInternal(String name, NodeType type, Map<String, String> properties)
    throws NodeNameExistsException {
        if (nodeExists(name)) {
            throw new NodeNameExistsException(name);
        }

        // add node to graph
        graph.put(name, getVertex(name, type, properties));
        if (type == NodeType.PC) {
            pcs.add(name);
        } else if (type == OA) {
            oas.add(name);
        } else if (type == UA) {
            uas.add(name);
        } else if (type == O) {
            os.add(name);
        } else if (type == U) {
            us.add(name);
        }
    }

    private void deleteInternal(String name) {
        Vertex vertex = graph.get(name);

        List<String> parents = vertex.getParents();
        List<Association> incomingAssociations = vertex.getIncomingAssociations();
        List<Association> outgoingAssociations = vertex.getOutgoingAssociations();

        for (String parent : parents) {
            graph.get(parent).deleteAssignment(name, parent);
        }

        for (Association association : incomingAssociations) {
            graph.get(association.getSource()).deleteAssociation(association.getSource(), association.getTarget());
        }

        for (Association association : outgoingAssociations) {
            graph.get(association.getTarget()).deleteAssociation(association.getSource(), association.getTarget());
        }

        graph.remove(name);

        if (vertex.getNode().getType() == PC) {
            pcs.remove(name);
        } else if (vertex.getNode().getType() == OA) {
            oas.remove(name);
        } else if (vertex.getNode().getType() == UA) {
            uas.remove(name);
        } else if (vertex.getNode().getType() == O) {
            os.remove(name);
        } else if (vertex.getNode().getType() == U) {
            us.remove(name);
        }
    }

    private Vertex getVertex(String name, NodeType type, Map<String, String> properties) {
        switch (type) {
            case PC -> {
                return new VertexPolicyClass(name, properties);
            }
            case OA -> {
                return new VertexObjectAttribute(name, properties);
            }
            case UA -> {
                return new VertexUserAttribute(name, properties);
            }
            case O -> {
                return new VertexObject(name, properties);
            }
            default -> {
                return new VertexUser(name, properties);
            }
        }
    }

    private List<String> filterByProperties(List<String> nodes, Map<String, String> properties) {
        List<String> results = new ArrayList<>();
        if (properties.isEmpty()) {
            results.addAll(nodes);
        } else {
            for (String n : nodes) {
                Map<String, String> nodeProperties = graph.get(n).getNode().getProperties();

                if (!hasAllKeys(nodeProperties, properties)
                        || !valuesMatch(nodeProperties, properties)) {
                    continue;
                }

                results.add(n);
            }
        }

        return results;
    }

    private List<String> filterByType(NodeType type) {
        List<String> nodes = new ArrayList<>();
        if (type != ANY) {
            if (type == PC) {
                nodes.addAll(pcs);
            } else if (type == OA) {
                nodes.addAll(oas);
            } else if (type == UA) {
                nodes.addAll(uas);
            } else if (type == O) {
                nodes.addAll(os);
            } else {
                nodes.addAll(us);
            }
        } else {
            nodes.addAll(pcs);
            nodes.addAll(uas);
            nodes.addAll(oas);
            nodes.addAll(us);
            nodes.addAll(os);
        }

        return nodes;
    }

    private boolean valuesMatch(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (Map.Entry<String, String> entry : checkProperties.entrySet()) {
            String checkKey = entry.getKey();
            String checkValue = entry.getValue();
            if (!checkValue.equals(nodeProperties.get(checkKey))
                    && !checkValue.equals(WILDCARD)) {
                return false;
            }
        }

        return true;
    }

    private boolean hasAllKeys(Map<String, String> nodeProperties, Map<String, String> checkProperties) {
        for (String key : checkProperties.keySet()) {
            if (!nodeProperties.containsKey(key)) {
                return false;
            }
        }

        return true;
    }

    protected void assignInternal(String child, String parent) {
        if (graph.get(child).getParents().contains(parent)) {
            return;
        }

        graph.get(child).addAssignment(child, parent);
        graph.get(parent).addAssignment(child, parent);
    }

    private void deassignInternal(String child, String parent) {
        graph.get(child).deleteAssignment(child, parent);
        graph.get(parent).deleteAssignment(child, parent);
    }

    protected void associateInternal(String ua, String target, AccessRightSet accessRights) {
        graph.get(ua).addAssociation(ua, target, accessRights);
        graph.get(target).addAssociation(ua, target, accessRights);
    }

    private void dissociateInternal(String ua, String target) {
        graph.get(ua).deleteAssociation(ua, target);
        graph.get(target).deleteAssociation(ua, target);
    }

    private boolean containsEdge(String source, String target) {
        return graph.get(source).getParents().contains(target)
                || graph.get(source).getOutgoingAssociations().contains(new Association(source, target));
    }

    private List<Node> getNodes() {
        Collection<Vertex> values = graph.values();
        List<Node> nodes = new ArrayList<>();
        for (Vertex v : values) {
            nodes.add(v.getNode());
        }
        return nodes;
    }
}
