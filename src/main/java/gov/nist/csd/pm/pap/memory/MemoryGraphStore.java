package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.GraphStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.exceptions.TransactionNotStartedException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.exceptions.NodeNameExistsException;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.Vertex;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.WILDCARD;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;

class MemoryGraphStore extends GraphStore {

    private final Graph graph;

    MemoryGraphStore() {
        this.graph = new Graph();
    }

    MemoryGraphStore(Graph graph) {
        this.graph = graph;
    }

    MemoryGraphStore(MemoryGraphStore graph) {
        this.graph = graph.graph;
    }

    Graph getGraph() {
        return graph;
    }

    @Override
    public synchronized void setResourceAccessRights(AccessRightSet accessRightSet) {
        graph.setResourceAccessRights(accessRightSet);
    }

    @Override
    public synchronized AccessRightSet getResourceAccessRights() {
        return new AccessRightSet(graph.getResourceAccessRights());
    }

    @Override
    public synchronized String createPolicyClass(String name, Map<String, String> properties) {
        return graph.createPolicyClass(name, properties);
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public synchronized String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return graph.createUserAttribute(name, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        return graph.createObjectAttribute(name, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        return graph.createObject(name, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        return graph.createUser(name, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
    }

    @Override
    public synchronized void setNodeProperties(String name, Map<String, String> properties) {
        graph.setNodeProperties(name, properties);
    }

    @Override
    public synchronized void deleteNode(String name) {
        graph.deleteNode(name);
    }

    @Override
    public synchronized boolean nodeExists(String name) {
        return graph.nodeExists(name);
    }

    @Override
    public synchronized Node getNode(String name) {
        Node node = graph.getNode(name);
        return new Node(node);
    }

    @Override
    public synchronized List<String> search(NodeType type, Map<String, String> checkProperties) {
       return graph.search(type, checkProperties);
    }

    @Override
    public synchronized List<String> getPolicyClasses() {
        return new ArrayList<>(graph.getPolicyClasses());
    }

    @Override
    public synchronized void assign(String child, String parent) {
        graph.assign(child, parent);
    }

    @Override
    public synchronized void deassign(String child, String parent) {
        graph.deassign(child, parent);
    }

    @Override
    public synchronized List<String> getChildren(String node) {
        return graph.getChildren(node);
    }

    @Override
    public synchronized List<String> getParents(String node) {
        return graph.getParents(node);
    }

    @Override
    public synchronized void associate(String ua, String target, AccessRightSet accessRights) {
        if (graph.containsEdge(ua, target)) {
            // remove the existing association edge in order to update it
            graph.dissociate(ua, target);
        }

        graph.associate(ua, target, accessRights);
    }

    @Override
    public synchronized void dissociate(String ua, String target) {
        graph.dissociate(ua, target);
    }

    @Override
    public synchronized List<Association> getAssociationsWithSource(String ua) {
        return graph.getAssociationsWithSource(ua);
    }

    @Override
    public synchronized List<Association> getAssociationsWithTarget(String target) {
        return graph.getAssociationsWithTarget(target);
    }

    @Override
    public synchronized void beginTx() {

    }

    @Override
    public synchronized void commit() throws TransactionNotStartedException {

    }

    @Override
    public synchronized void rollback() throws TransactionNotStartedException {

    }
}
