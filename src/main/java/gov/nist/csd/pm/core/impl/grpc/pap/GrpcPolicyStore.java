package gov.nist.csd.pm.core.impl.grpc.pap;

import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.model.subgraph.Subgraph;
import gov.nist.csd.pm.core.pap.store.GraphStore;
import gov.nist.csd.pm.core.pap.store.ObligationsStore;
import gov.nist.csd.pm.core.pap.store.OperationsStore;
import gov.nist.csd.pm.core.pap.store.PolicyStore;
import gov.nist.csd.pm.core.pap.store.ProhibitionsStore;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrpcPolicyStore implements PolicyStore {

    private NoopGraphStore noopGraphStore;
    private NoopProhibitionsStore noopProhibitionsStore;
    private NoopObligationsStore noopObligationsStore;
    private NoopOperationsStore noopOperationsStore;

    public GrpcPolicyStore() {
        this.noopGraphStore = new NoopGraphStore();
        this.noopProhibitionsStore = new NoopProhibitionsStore();
        this.noopObligationsStore = new NoopObligationsStore();
        this.noopOperationsStore = new NoopOperationsStore();
    }

    @Override
    public GraphStore graph() {
        return noopGraphStore;
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return noopProhibitionsStore;
    }

    @Override
    public ObligationsStore obligations() {
        return noopObligationsStore;
    }

    @Override
    public OperationsStore operations() {
        return noopOperationsStore;
    }

    @Override
    public void reset() {

    }

    @Override
    public void beginTx() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
    
    private static class NoopGraphStore implements GraphStore {

        @Override
        public void createNode(long id, String name, NodeType type) {
            
        }

        @Override
        public void deleteNode(long id) {

        }

        @Override
        public void setNodeProperties(long id, Map<String, String> properties) {

        }

        @Override
        public void createAssignment(long start, long end) {

        }

        @Override
        public void deleteAssignment(long start, long end) {

        }

        @Override
        public void createAssociation(long ua, long target, AccessRightSet arset) {

        }

        @Override
        public void deleteAssociation(long ua, long target) {

        }

        @Override
        public Node getNodeById(long id) {
            return null;
        }

        @Override
        public Node getNodeByName(String name) {
            return null;
        }

        @Override
        public boolean nodeExists(long id) {
            return false;
        }

        @Override
        public boolean nodeExists(String name) {
            return false;
        }

        @Override
        public Collection<Long> search(NodeType type, Map<String, String> properties) {
            return List.of();
        }

        @Override
        public Collection<Long> getPolicyClasses() {
            return List.of();
        }

        @Override
        public Collection<Long> getAdjacentDescendants(long id) {
            return List.of();
        }

        @Override
        public Collection<Long> getAdjacentAscendants(long id) {
            return List.of();
        }

        @Override
        public Collection<Association> getAssociationsWithSource(long uaId) {
            return List.of();
        }

        @Override
        public Collection<Association> getAssociationsWithTarget(long targetId) {
            return List.of();
        }

        @Override
        public Collection<Long> getPolicyClassDescendants(long id) {
            return List.of();
        }

        @Override
        public Collection<Long> getAttributeDescendants(long id) {
            return List.of();
        }

        @Override
        public Subgraph getDescendantSubgraph(long id) {
            return null;
        }

        @Override
        public Subgraph getAscendantSubgraph(long id) {
            return null;
        }

        @Override
        public boolean isAscendant(long asc, long dsc) {
            return false;
        }

        @Override
        public boolean isDescendant(long asc, long dsc) {
            return false;
        }

        @Override
        public void beginTx() {

        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }
    }
    
    private static class NoopProhibitionsStore implements ProhibitionsStore {

        @Override
        public void createNodeProhibition(String name,
                                          long nodeId,
                                          AccessRightSet accessRightSet,
                                          Set<Long> inclusionSet,
                                          Set<Long> exclusionSet,
                                          boolean isConjunctive) {
            
        }

        @Override
        public void createProcessProhibition(String name,
                                             long userId,
                                             String process,
                                             AccessRightSet accessRightSet,
                                             Set<Long> inclusionSet,
                                             Set<Long> exclusionSet,
                                             boolean isConjunctive) {

        }

        @Override
        public void deleteProhibition(String name) {

        }

        @Override
        public Collection<Prohibition> getAllProhibitions() {
            return List.of();
        }

        @Override
        public Collection<Prohibition> getNodeProhibitions(long nodeId) {
            return List.of();
        }

        @Override
        public Collection<Prohibition> getProcessProhibitions(String process) {
            return List.of();
        }

        @Override
        public Prohibition getProhibition(String name) {
            return null;
        }

        @Override
        public boolean prohibitionExists(String name) {
            return false;
        }

        @Override
        public void beginTx() {

        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }
    }
    
    private static class NoopObligationsStore implements ObligationsStore {

        @Override
        public void beginTx() {
            
        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }

        @Override
        public void createObligation(long authorId,
                                     String name,
                                     EventPattern eventPattern,
                                     ObligationResponse response) {

        }

        @Override
        public void deleteObligation(String name) {

        }

        @Override
        public Collection<Obligation> getObligations() {
            return List.of();
        }

        @Override
        public boolean obligationExists(String name) {
            return false;
        }

        @Override
        public Obligation getObligation(String name) {
            return null;
        }

        @Override
        public Collection<Obligation> getObligationsWithAuthor(long userId) {
            return List.of();
        }
    }
    
    private static class NoopOperationsStore implements OperationsStore {

        @Override
        public void beginTx() {
            
        }

        @Override
        public void commit() {

        }

        @Override
        public void rollback() {

        }

        @Override
        public void setResourceAccessRights(AccessRightSet resourceAccessRights) {

        }

        @Override
        public void createOperation(Operation<?> operation) {

        }

        @Override
        public void deleteOperation(String name) {

        }

        @Override
        public AccessRightSet getResourceAccessRights() {
            return null;
        }

        @Override
        public Collection<Operation<?>> getOperations() {
            return List.of();
        }

        @Override
        public Collection<String> getOperationNames() {
            return List.of();
        }

        @Override
        public Operation<?> getOperation(String name) {
            return null;
        }

        @Override
        public boolean operationExists(String name) {
            return false;
        }
    }
}
