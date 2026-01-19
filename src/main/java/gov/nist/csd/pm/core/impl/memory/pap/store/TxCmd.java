package gov.nist.csd.pm.core.impl.memory.pap.store;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.function.Routine;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import java.util.Collection;
import java.util.Map;

public abstract class TxCmd implements TxRollbackSupport {
    
    static class SetResourceOperationsTxCmd extends TxCmd {

        private final AccessRightSet oldAccessRights;
        private final AccessRightSet newAccessRights;

        public SetResourceOperationsTxCmd(AccessRightSet oldAccessRights, AccessRightSet newAccessRights) {
            this.oldAccessRights = oldAccessRights;
            this.newAccessRights = newAccessRights;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.operations().setResourceAccessRights(oldAccessRights);
        }
    }

    static class CreateNodeTxCmd extends TxCmd {

        private final long id;

        public CreateNodeTxCmd(long id) {
            this.id = id;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteNode(id);
        }
    }

    static class SetNodePropertiesTxCmd extends TxCmd {
        private final long id;
        private final Map<String, String> oldProperties;

        public SetNodePropertiesTxCmd(long id, Map<String, String> oldProperties) {
            this.id = id;
            this.oldProperties = oldProperties;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().setNodeProperties(id, oldProperties);
        }
    }

    static class DeleteNodeTxCmd extends TxCmd {
        private final long id;
        private final Node nodeToDelete;
        private final Collection<Long> descendants;

        public DeleteNodeTxCmd(long id, Node nodeToDelete, Collection<Long> descendants) {
            this.id = id;
            this.nodeToDelete = nodeToDelete;
            this.descendants = descendants;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            NodeType type = nodeToDelete.getType();
            Map<String, String> properties = nodeToDelete.getProperties();

            memoryPolicyStore.graph().createNode(id, nodeToDelete.getName(), nodeToDelete.getType());

            for (long descendant : descendants) {
                memoryPolicyStore.graph().createAssignment(id, descendant);
            }

            memoryPolicyStore.graph().setNodeProperties(id, properties);
        }
    }

    static final class CreateAssignmentTxCmd extends TxCmd {
        private final long ascendant;
        private final long descendant;

        public CreateAssignmentTxCmd(long ascendant, long descendant) {
            this.ascendant = ascendant;
            this.descendant = descendant;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteAssignment(ascendant, descendant);
        }
    }

    static class DeleteAssignmentTxCmd extends TxCmd {
        private final long ascendant;
        private final long descendant;

        public DeleteAssignmentTxCmd(long ascendant, long descendant) {
            this.ascendant = ascendant;
            this.descendant = descendant;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().createAssignment(ascendant, descendant);
        }
    }

    static class CreateAssociationTxCmd extends TxCmd {

        private final long source;
        private final long target;

        public CreateAssociationTxCmd(long source, long target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.graph().deleteAssociation(source, target);
        }
    }

    static class DeleteAssociationTxCmd extends TxCmd {
        private final long ua;
        private final long target;
        private final AccessRightSet accessRightSet;

        public DeleteAssociationTxCmd(long ua, long target, AccessRightSet accessRightSet) {
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
                    obligationToDelete.getAuthorId(),
                    obligationToDelete.getName(),
                    obligationToDelete.getEventPattern(),
                    obligationToDelete.getResponse()
            );
        }
    }

    static class CreateAdminRoutine extends TxCmd {

        private final Routine<?> routine;

        public CreateAdminRoutine(Routine<?> routine) {
            this.routine = routine;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.operations().deleteAdminRoutine(routine.getName());
        }
    }

    static class DeleteAdminRoutine extends TxCmd {

        private final Routine<?> routine;

        public DeleteAdminRoutine(Routine<?> routine) {
            this.routine = routine;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.operations().createAdminRoutine(routine);
        }
    }
}
