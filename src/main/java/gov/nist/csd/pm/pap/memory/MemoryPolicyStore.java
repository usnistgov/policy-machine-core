package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.PolicyStore;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.ObligationDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.ProhibitionDoesNotExistException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.Graph;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.events.PolicySynchronizationEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static gov.nist.csd.pm.policy.tx.TxRunner.runTx;

public class MemoryPolicyStore extends PolicyStore {

    private Graph graph;
    private Map<String, List<Prohibition>> prohibitions;
    private List<Obligation> obligations;
    private PALContext pal;

    private boolean inTx;
    private int txCounter;
    private TxPolicyStore txPolicyStore;

    public MemoryPolicyStore() {
        graph = new Graph();
        prohibitions = new HashMap<>();
        obligations = new ArrayList<>();
        pal = new PALContext();
    }

    public MemoryPolicyStore(PolicySynchronizationEvent event) {
        graph = event.getGraph();
        prohibitions = event.getProhibitions();
        obligations = event.getObligations();
        pal = event.getPALContext();
    }

    public MemoryPolicyStore(Graph graph, Map<String, List<Prohibition>> prohibitions, List<Obligation> obligations,
                             PALContext pal) {
        this.graph = new Graph(graph);
        this.prohibitions = copyProhibitions(prohibitions);
        this.obligations = new ArrayList<>(obligations);
        this.pal = copyPalCtx(pal);
    }

    protected Map<String, List<Prohibition>> copyProhibitions(Map<String, List<Prohibition>> toCopy) {
        HashMap<String, List<Prohibition>> prohibitions = new HashMap<>();
        for (String subject : toCopy.keySet()) {
            List<Prohibition> copyPros = new ArrayList<>();
            for (Prohibition p : toCopy.get(subject)) {
                copyPros.add(new Prohibition(p));
            }

            prohibitions.put(subject, copyPros);
        }

        return prohibitions;
    }

    protected PALContext copyPalCtx(PALContext pal) {
        PALContext ctx = new PALContext();

        Map<String, FunctionDefinitionStatement> functions = pal.getFunctions();
        for (Map.Entry<String, FunctionDefinitionStatement> entry : functions.entrySet()) {
            functions.put(entry.getKey(), new FunctionDefinitionStatement(entry.getValue()));
        }

        Map<String, Value> constants = pal.getConstants();
        for (Map.Entry<String, Value> entry : constants.entrySet()) {
            constants.put(entry.getKey(), entry.getValue());
        }

        return ctx;
    }

    @Override
    public synchronized PolicySynchronizationEvent policySync() {
        return new PolicySynchronizationEvent(
                graph,
                prohibitions,
                obligations,
                pal
        );
    }

    @Override
    public synchronized void beginTx() throws PMException {
        if (!inTx) {
            txPolicyStore = new TxPolicyStore(this);
        }

        inTx = true;
        txCounter++;
    }

    @Override
    public synchronized void commit() throws PMException {
        txCounter--;
        if(txCounter == 0) {
            inTx = false;
            txPolicyStore.clearEvents();
        }
    }

    @Override
    public synchronized void rollback() throws PMException {
        inTx = false;
        txCounter = 0;

        TxPolicyEventListener txPolicyEventListener = txPolicyStore.getTxPolicyEventListener();
        txPolicyEventListener.revert(this);
    }

    @Override
    public synchronized void setResourceAccessRights(AccessRightSet accessRightSet) {
        if (inTx) {
            txPolicyStore.setResourceAccessRights(accessRightSet);
        }

        graph.setResourceAccessRights(accessRightSet);
    }

    @Override
    public synchronized String createPolicyClass(String name, Map<String, String> properties) {
        if (inTx) {
            txPolicyStore.createPolicyClass(name, properties);
        }

        return graph.createPolicyClass(name, properties);
    }

    @Override
    public String createPolicyClass(String name) throws PMException {
        return createPolicyClass(name, noprops());
    }

    @Override
    public synchronized String createUserAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        if (inTx) {
            txPolicyStore.createUserAttribute(name, properties, parent, parents);
        }

        return graph.createUserAttribute(name, properties, parent, parents);
    }

    @Override
    public String createUserAttribute(String name, String parent, String... parents) throws PMException {
        return createUserAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObjectAttribute(String name, Map<String, String> properties, String parent, String... parents) {
        if (inTx) {
            txPolicyStore.createObjectAttribute(name, properties, parent, parents);
        }

        return graph.createObjectAttribute(name, properties, parent, parents);
    }

    @Override
    public String createObjectAttribute(String name, String parent, String... parents) throws PMException {
        return createObjectAttribute(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createObject(String name, Map<String, String> properties, String parent, String... parents) {
        if (inTx) {
            txPolicyStore.createObject(name, properties, parent, parents);
        }

        return graph.createObject(name, properties, parent, parents);
    }

    @Override
    public String createObject(String name, String parent, String... parents) throws PMException {
        return createObject(name, noprops(), parent, parents);
    }

    @Override
    public synchronized String createUser(String name, Map<String, String> properties, String parent, String... parents) {
        if (inTx) {
            txPolicyStore.createUser(name, properties, parent, parents);
        }

        return graph.createUser(name, properties, parent, parents);
    }

    @Override
    public String createUser(String name, String parent, String... parents) throws PMException {
        return createUser(name, noprops(), parent, parents);
    }

    @Override
    public synchronized void setNodeProperties(String name, Map<String, String> properties) {
        if (inTx) {
            txPolicyStore.setNodeProperties(name, properties);
        }

        graph.setNodeProperties(name, properties);
    }

    @Override
    public synchronized void deleteNode(String name) {
        if (inTx) {
            txPolicyStore.deleteNode(name);
        }

        graph.deleteNode(name);
    }

    @Override
    public synchronized void assign(String child, String parent) {
        if (inTx) {
            txPolicyStore.assign(child, parent);
        }

        graph.assign(child, parent);
    }

    @Override
    public synchronized void deassign(String child, String parent) {
        if (inTx) {
            txPolicyStore.deassign(child, parent);
        }

        graph.deassign(child, parent);
    }

    @Override
    public void assignAll(List<String> children, String target) throws PMException {

    }

    @Override
    public void deassignAll(List<String> children, String target) throws PMException {

    }

    @Override
    public void deassignAllFromAndDelete(String target) throws PMException {

    }

    @Override
    public synchronized void associate(String ua, String target, AccessRightSet accessRights) {
        if (inTx) {
            txPolicyStore.associate(ua, target, accessRights);
        }

        if (graph.containsEdge(ua, target)) {
            // remove the existing association edge in order to update it
            graph.dissociate(ua, target);
        }

        graph.associate(ua, target, accessRights);
    }

    @Override
    public synchronized void dissociate(String ua, String target) {
        if (inTx) {
            txPolicyStore.dissociate(ua, target);
        }

        graph.dissociate(ua, target);
    }

    @Override
    public void createProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) {
        if (inTx) {
            txPolicyStore.createProhibition(label, subject, accessRightSet, intersection, containerConditions);
        }

        List<Prohibition> existingPros = prohibitions.getOrDefault(subject.name(), new ArrayList<>());
        existingPros.add(new Prohibition(label, subject, accessRightSet, intersection, Arrays.asList(containerConditions)));
        prohibitions.put(subject.name(), existingPros);
    }

    @Override
    public void updateProhibition(String label, ProhibitionSubject subject, AccessRightSet accessRightSet, boolean intersection, ContainerCondition... containerConditions) throws PMException {
        if (inTx) {
            txPolicyStore.updateProhibition(label, subject, accessRightSet, intersection, containerConditions);
        }

        runTx(this, () -> {
            deleteProhibition(label);
            createProhibition(label, subject, accessRightSet, intersection, containerConditions);
        });
    }

    @Override
    public void deleteProhibition(String label) throws PMException {
        if (inTx) {
            txPolicyStore.deleteProhibition(label);
        }

        for(String subject : prohibitions.keySet()) {
            List<Prohibition> ps = prohibitions.get(subject);
            Iterator<Prohibition> iterator = ps.iterator();
            while (iterator.hasNext()) {
                Prohibition p = iterator.next();
                if(p.getLabel().equals(label)) {
                    iterator.remove();
                    prohibitions.put(subject, ps);
                }
            }
        }
    }

    @Override
    public void createObligation(UserContext author, String label, Rule... rules) {
        if (inTx) {
            txPolicyStore.createObligation(author, label, rules);
        }

        obligations.add(new Obligation(author, label, Arrays.asList(rules)));
    }

    @Override
    public void updateObligation(UserContext author, String label, Rule... rules) throws PMException {
        if (inTx) {
            txPolicyStore.updateObligation(author, label, rules);
        }

        for (Obligation o : obligations) {
            if (o.getLabel().equals(label)) {
                o.setAuthor(author);
                o.setLabel(label);
                o.setRules(List.of(rules));
            }
        }
    }

    @Override
    public void deleteObligation(String label) throws PMException {
        if (inTx) {
            txPolicyStore.deleteObligation(label);
        }

        this.obligations.removeIf(o -> o.getLabel().equals(label));
    }

    @Override
    public void addPALFunction(FunctionDefinitionStatement functionDefinitionStatement) {
        if (inTx) {
            txPolicyStore.addPALFunction(functionDefinitionStatement);
        }

        if (functionDefinitionStatement.isFunctionExecutor()) {
            pal.addFunction(new FunctionDefinitionStatement(
                    functionDefinitionStatement.getFunctionName(),
                    functionDefinitionStatement.getReturnType(),
                    new ArrayList<>(functionDefinitionStatement.getArgs()),
                    functionDefinitionStatement.getFunctionExecutor()
            ));
        } else {
            pal.addFunction(new FunctionDefinitionStatement(
                    functionDefinitionStatement.getFunctionName(),
                    functionDefinitionStatement.getReturnType(),
                    new ArrayList<>(functionDefinitionStatement.getArgs()),
                    new ArrayList<>(functionDefinitionStatement.getBody())
            ));
        }
    }

    @Override
    public void removePALFunction(String functionName) {
        if (inTx) {
            txPolicyStore.removePALFunction(functionName);
        }

        pal.removeFunction(functionName);
    }

    @Override
    public void addPALConstant(String constantName, Value constantValue) {
        if (inTx) {
            txPolicyStore.addPALConstant(constantName, constantValue);
        }

        pal.addConstant(constantName, constantValue);
    }

    @Override
    public void removePALConstant(String constName) {
        if (inTx) {
            txPolicyStore.removePALConstant(constName);
        }

        pal.removeConstant(constName);
    }

    @Override
    public synchronized AccessRightSet getResourceAccessRights() {
        return new AccessRightSet(graph.getResourceAccessRights());
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
    public synchronized List<String> getChildren(String node) {
        return graph.getChildren(node);
    }

    @Override
    public synchronized List<String> getParents(String node) {
        return graph.getParents(node);
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
    public Map<String, List<Prohibition>> getProhibitions() {
        Map<String, List<Prohibition>> retProhibitions = new HashMap<>();
        for (String subject : prohibitions.keySet()) {
            retProhibitions.put(subject, prohibitions.get(subject));
        }

        return retProhibitions;
    }

    @Override
    public boolean prohibitionExists(String label) throws PMException {
        for (Map.Entry<String, List<Prohibition>> e : prohibitions.entrySet()) {
            for (Prohibition p : e.getValue()) {
                if (p.getLabel().equals(label)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public List<Prohibition> getProhibitionsWithSubject(String subject) {
        List<Prohibition> subjectPros = prohibitions.get(subject);
        if (subjectPros == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(subjectPros);
    }

    @Override
    public Prohibition getProhibition(String label) throws PMException {
        for (String subject : prohibitions.keySet()) {
            List<Prohibition> subjectPros = prohibitions.get(subject);
            for (Prohibition p : subjectPros) {
                if (p.getLabel().equals(label)) {
                    return p;
                }
            }
        }

        throw new ProhibitionDoesNotExistException(label);
    }

    @Override
    public List<Obligation> getObligations() {
        return new ArrayList<>(obligations);
    }

    @Override
    public boolean obligationExists(String label) throws PMException {
        for (Obligation o : obligations) {
            if (o.getLabel().equals(label)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Obligation getObligation(String label) throws PMException {
        for (Obligation obligation : obligations) {
            if (obligation.getLabel().equals(label)) {
                return obligation.clone();
            }
        }

        throw new ObligationDoesNotExistException(label);
    }

    @Override
    public Map<String, FunctionDefinitionStatement> getPALFunctions() {
        return new HashMap<>(pal.getFunctions());
    }

    @Override
    public Map<String, Value> getPALConstants() {
        return new HashMap<>(pal.getConstants());
    }

    @Override
    public PALContext getPALContext() {
        return pal;
    }

    @Override
    protected void reset() {
        graph = new Graph();
        prohibitions = new HashMap<>();
        obligations = new ArrayList<>();
        pal = new PALContext();
    }
}
