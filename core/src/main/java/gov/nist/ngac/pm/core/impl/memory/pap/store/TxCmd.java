/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.impl.memory.pap.store;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.common.graph.node.NodeType;
import gov.nist.ngac.pm.core.common.prohibition.NodeProhibition;
import gov.nist.ngac.pm.core.common.prohibition.ProcessProhibition;
import gov.nist.ngac.pm.core.common.prohibition.Prohibition;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.Collection;
import java.util.Map;

/**
 * Base class for the undo commands used to roll back an in-memory transaction.
 */
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
            switch (prohibitionToDelete) {
                case NodeProhibition nodeProhibition -> memoryPolicyStore.prohibitions().createNodeProhibition(
                    nodeProhibition.getName(),
                    nodeProhibition.getNodeId(),
                    nodeProhibition.getAccessRightSet(),
                    nodeProhibition.getInclusionSet(),
                    nodeProhibition.getExclusionSet(),
                    nodeProhibition.isConjunctive()
                );
                case ProcessProhibition processProhibition -> memoryPolicyStore.prohibitions().createProcessProhibition(
                    processProhibition.getName(),
                    processProhibition.getUserId(),
                    processProhibition.getProcess(),
                    processProhibition.getAccessRightSet(),
                    processProhibition.getInclusionSet(),
                    processProhibition.getExclusionSet(),
                    processProhibition.isConjunctive()
                );
            }
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
            memoryPolicyStore.obligations().createObligation(obligationToDelete);
        }
    }

    static class CreateOperationTxCmd extends TxCmd {
        private final Operation<?> operation;

        public CreateOperationTxCmd(Operation<?> operation) {
            this.operation = operation;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            memoryPolicyStore.operations().deleteOperation(operation.getName());
        }
    }

    static class DeleteOperation extends TxCmd {

        private final Operation<?> operation;

        public DeleteOperation(Operation<?> operation) {
            this.operation = operation;
        }

        @Override
        public void rollback(MemoryPolicyStore memoryPolicyStore) throws PMException {
            MemoryOperationsStore opsStore = memoryPolicyStore.operations();
            opsStore.createOperation(operation);
        }
    }
}
