package gov.nist.csd.pm.impl.memory.pap.store;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.prohibition.Prohibition;
import gov.nist.csd.pm.pap.routine.Routine;

import java.util.Collection;
import java.util.Map;

public abstract class TxCmd implements TxRollbackSupport {
    
    static class SetResourceOperationsTxCmd extends TxCmd {

        private AccessRightSet oldAccessRights;
        private AccessRightSet newAccessRights;

        public SetResourceOperationsTxCmd(AccessRightSet oldAccessRights, AccessRightSet newAccessRights) {
            this.oldAccessRights = oldAccessRights;
            this.newAccessRights = newAccessRights;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.operations().setResourceOperations(oldAccessRights);
        }
    }

    static class CreatePolicyClassTxCmd extends TxCmd {
            
        private String name;
        private Map<String, String> properties;

        public CreatePolicyClassTxCmd(String name, Map<String, String> properties) {
            this.name = name;
            this.properties = properties;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteNode(name);
        }
    }

    static class CreateNodeTxCmd extends TxCmd {

        private final String name;

        public CreateNodeTxCmd(String name) {
            this.name = name;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteNode(name);
        }
    }

    static class SetNodePropertiesTxCmd extends TxCmd {
        private final String name;
        private final Map<String, String> oldProperties;
        private final Map<String, String> newProperties;

        public SetNodePropertiesTxCmd(String name, Map<String, String> oldProperties, Map<String, String> newProperties) {
            this.name = name;
            this.oldProperties = oldProperties;
            this.newProperties = newProperties;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().setNodeProperties(name, oldProperties);
        }
    }

    static class DeleteNodeTxCmd extends TxCmd {
        private final String name;
        private final Node nodeToDelete;
        private final Collection<String> descendants;

        public DeleteNodeTxCmd(String name, Node nodeToDelete, Collection<String> descendants) {
            this.name = name;
            this.nodeToDelete = nodeToDelete;
            this.descendants = descendants;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            NodeType type = nodeToDelete.getType();
            Map<String, String> properties = nodeToDelete.getProperties();

            memoryPolicyStore.graph().createNode(name, nodeToDelete.getType());

            for (String descendant : descendants) {
                memoryPolicyStore.graph().createAssignment(name, descendant);
            }
        }
    }

    static final class CreateAssignmentTxCmd extends TxCmd {
        private final String ascendant;
        private final String descendant;

        public CreateAssignmentTxCmd(String ascendant, String descendant) {
            this.ascendant = ascendant;
            this.descendant = descendant;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteAssignment(ascendant, descendant);
        }
    }

    static class DeleteAssignmentTxCmd extends TxCmd {
        private final String ascendant;
        private final String descendant;

        public DeleteAssignmentTxCmd(String ascendant, String descendant) {
            this.ascendant = ascendant;
            this.descendant = descendant;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().createAssignment(ascendant, descendant);
        }
    }

    static class CreateAssociationTxCmd extends TxCmd {

        private final String source;
        private final String target;

        public CreateAssociationTxCmd(String source, String target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteAssociation(source, target);
        }
    }

    static class DeleteAssociationTxCmd extends TxCmd {
        private String ua;
        private String target;
        private AccessRightSet accessRightSet;

        public DeleteAssociationTxCmd(String ua, String target, AccessRightSet accessRightSet) {
            this.ua = ua;
            this.target = target;
            this.accessRightSet = accessRightSet;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().createAssociation(ua, target, accessRightSet);
        }
    }

    static class CreateProhibitionTxCmd extends TxCmd {
        private final Prohibition prohibition;

        public CreateProhibitionTxCmd(Prohibition prohibition) {
            this.prohibition = prohibition;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.prohibitions().deleteProhibition(prohibition.getName());
        }
    }

    static class DeleteProhibitionTxCmd extends TxCmd {
        private final Prohibition prohibitionToDelete;

        public DeleteProhibitionTxCmd(Prohibition prohibitionToDelete) {
            this.prohibitionToDelete = prohibitionToDelete;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.prohibitions().createProhibition(
                    prohibitionToDelete.getName(),
                    prohibitionToDelete.getSubject(),
                    prohibitionToDelete.getAccessRightSet(),
                    prohibitionToDelete.isIntersection(),
                    prohibitionToDelete.getContainers()
            );
        }
    }

    static class CreateObligationTxCmd extends TxCmd {
        private final Obligation obligation;

        public CreateObligationTxCmd(Obligation obligation) {
            this.obligation = obligation;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.obligations().deleteObligation(obligation.getName());
        }
    }

    static class DeleteObligationTxCmd extends TxCmd {
        private final Obligation obligationToDelete;
        public DeleteObligationTxCmd(Obligation obligationToDelete) {
            this.obligationToDelete = obligationToDelete;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.obligations().createObligation(
                    obligationToDelete.getAuthor(),
                    obligationToDelete.getName(),
                    obligationToDelete.getRules()
            );
        }
    }

    static class CreateAdminRoutine extends TxCmd {

        private Routine<?> routine;

        public CreateAdminRoutine(Routine<?> routine) {
            this.routine = routine;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.routines().deleteAdminRoutine(routine.getName());
        }
    }

    static class DeleteAdminRoutine extends TxCmd {

        private Routine<?> routine;

        public DeleteAdminRoutine(Routine<?> routine) {
            this.routine = routine;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.routines().createAdminRoutine(routine);
        }
    }
}
