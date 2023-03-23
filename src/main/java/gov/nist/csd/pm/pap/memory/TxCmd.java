package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.NodeType;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.*;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;

interface TxCmd {

    void revert(MemoryPolicyStore store) throws PMException;
    
    class CreatePolicyClassTxCmd implements TxCmd {
            
        private String name;
        private Map<String, String> properties;

        public CreatePolicyClassTxCmd(String name, Map<String, String> properties) {
            this.name = name;
            this.properties = properties;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deleteNode(name);
        }
    }

    class CreateObjectAttributeTxCmd implements TxCmd {
        private final String name;
        private final Map<String, String> properties;
        private final String parent;
        private final String[] parents;

        public CreateObjectAttributeTxCmd(String name, Map<String, String> properties, String parent, String... parents) {
            this.name = name;
            this.properties = properties;
            this.parent = parent;
            this.parents = parents;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deleteNode(name);
        }
    }

    class CreateUserAttributeTxCmd implements TxCmd {
        private final String name;
        private final Map<String, String> properties;
        private final String parent;
        private final String[] parents;

        public CreateUserAttributeTxCmd(String name, Map<String, String> properties, String parent, String... parents) {
            this.name = name;
            this.properties = properties;
            this.parent = parent;
            this.parents = parents;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deleteNode(name);
        }
    }

    class CreateObjectTxCmd implements TxCmd {
        private final String name;
        private final Map<String, String> properties;
        private final String parent;
        private final String[] parents;

        public CreateObjectTxCmd(String name, Map<String, String> properties, String parent, String... parents) {
            this.name = name;
            this.properties = properties;
            this.parent = parent;
            this.parents = parents;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deleteNode(name);
        }
    }

    class CreateUserTxCmd implements TxCmd {
        private final String name;
        private final Map<String, String> properties;
        private final String parent;
        private final String[] parents;

        public CreateUserTxCmd(String name, Map<String, String> properties, String parent, String... parents) {
            this.name = name;
            this.properties = properties;
            this.parent = parent;
            this.parents = parents;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deleteNode(name);
        }
    }

    class SetNodePropertiesTxCmd implements TxCmd {
        private final String name;
        private final Map<String, String> oldProperties;
        private final Map<String, String> newProperties;

        public SetNodePropertiesTxCmd(String name, Map<String, String> oldProperties, Map<String, String> newProperties) {
            this.name = name;
            this.oldProperties = oldProperties;
            this.newProperties = newProperties;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.setNodeProperties(name, oldProperties);
        }
    }

    class DeleteNodeTxCmd implements TxCmd {
        private final String name;
        private final Node nodeToDelete;
        private final List<String> parents;

        public DeleteNodeTxCmd(String name, Node nodeToDelete, List<String> parents) {
            this.name = name;
            this.nodeToDelete = nodeToDelete;
            this.parents = parents;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            NodeType type = nodeToDelete.getType();
            Map<String, String> properties = nodeToDelete.getProperties();
            String initialParent = "";
            String[] parentsArr = new String[parents.size()];
            if (type != PC) {
                initialParent = parents.get(0);
                parents.remove(0);
                parentsArr = parents.toArray(new String[]{});
            }

            switch (type) {
                case PC -> store.createPolicyClass(name, properties);
                case OA -> store.createObjectAttribute(name, properties, initialParent, parentsArr);
                case UA -> store.createUserAttribute(name, properties, initialParent, parentsArr);
                case O -> store.createObject(name, properties, initialParent, parentsArr);
                case U -> store.createUser(name, properties, initialParent, parentsArr);
            }
        }
    }

    final class AssignTxCmd implements TxCmd {
        private final String child;
        private final String parent;

        public AssignTxCmd(String child, String parent) {
            this.child = child;
            this.parent = parent;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.deassign(child, parent);
        }
    }

    class DeassignTxCmd implements TxCmd {
        private final String child;
        private final String parent;

        public DeassignTxCmd(String child, String parent) {
            this.child = child;
            this.parent = parent;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.assign(child, parent);
        }
    }

    class AssociateTxCmd implements TxCmd {
        private final Association association;

        public AssociateTxCmd(Association association) {
            this.association = association;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.dissociate(association.getSource(), association.getTarget());
        }
    }

    class DissociateTxCmd implements TxCmd {
        private final Association association;

        public DissociateTxCmd(Association association) {
            this.association = association;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.associate(association.getSource(), association.getTarget(), association.getAccessRightSet());
        }
    }

    class CreateProhibitionTxCmd implements TxCmd {
        private final Prohibition prohibition;

        public CreateProhibitionTxCmd(Prohibition prohibition) {
            this.prohibition = prohibition;
        }

        @Override
        public void revert(MemoryPolicyStore store) throws PMException {
            store.deleteProhibition(prohibition.getLabel());
        }
    }

    class UpdateProhibitionTxCmd implements TxCmd {
        private final Prohibition newProhibition;
        private final Prohibition oldProhibition;

        public UpdateProhibitionTxCmd(Prohibition newProhibition, Prohibition oldProhibition) {
            this.newProhibition = newProhibition;
            this.oldProhibition = oldProhibition;
        }

        @Override
        public void revert(MemoryPolicyStore store) throws PMException {
            store.updateProhibition(
                    oldProhibition.getLabel(),
                    oldProhibition.getSubject(),
                    oldProhibition.getAccessRightSet(),
                    oldProhibition.isIntersection(),
                    oldProhibition.getContainers().toArray(new ContainerCondition[]{})
            );
        }
    }

    class DeleteProhibitionTxCmd implements TxCmd {
        private final Prohibition prohibitionToDelete;

        public DeleteProhibitionTxCmd(Prohibition prohibitionToDelete) {
            this.prohibitionToDelete = prohibitionToDelete;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.createProhibition(
                    prohibitionToDelete.getLabel(),
                    prohibitionToDelete.getSubject(),
                    prohibitionToDelete.getAccessRightSet(),
                    prohibitionToDelete.isIntersection(),
                    prohibitionToDelete.getContainers().toArray(new ContainerCondition[]{})
            );
        }
    }

    class CreateObligationTxCmd implements TxCmd {
        private final Obligation obligation;

        public CreateObligationTxCmd(Obligation obligation) {
            this.obligation = obligation;
        }

        @Override
        public void revert(MemoryPolicyStore store) throws PMException {
            store.deleteObligation(obligation.getLabel());
        }
    }

    class UpdateObligationTxCmd implements TxCmd {
        private final Obligation newObligation;
        private final Obligation oldObligation;

        public UpdateObligationTxCmd(Obligation newObligation, Obligation oldObligation) {
            this.newObligation = newObligation;
            this.oldObligation = oldObligation;
        }

        @Override
        public void revert(MemoryPolicyStore store) throws PMException {
            store.updateObligation(
                    oldObligation.getAuthor(),
                    oldObligation.getLabel(),
                    oldObligation.getRules().toArray(new Rule[]{})
            );
        }
    }

    class DeleteObligationTxCmd implements TxCmd {
        private final Obligation obligationToDelete;
        public DeleteObligationTxCmd(Obligation obligationToDelete) {
            this.obligationToDelete = obligationToDelete;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.createObligation(
                    obligationToDelete.getAuthor(),
                    obligationToDelete.getLabel(),
                    obligationToDelete.getRules().toArray(new Rule[]{})
            );
        }
    }

    class AddFunctionTxCmd implements TxCmd {
        private final FunctionDefinitionStatement functionDefinitionStatement;

        public AddFunctionTxCmd(FunctionDefinitionStatement functionDefinitionStatement) {
            this.functionDefinitionStatement = functionDefinitionStatement;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.removePALFunction(functionDefinitionStatement.getFunctionName());
        }
    }

    class RemoveFunctionTxCmd implements TxCmd {
        private final FunctionDefinitionStatement functionDefinitionStatement;

        public RemoveFunctionTxCmd(FunctionDefinitionStatement functionDefinitionStatement) {
            this.functionDefinitionStatement = functionDefinitionStatement;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.addPALFunction(functionDefinitionStatement);
        }
        
    }

    class AddConstantTxCmd implements TxCmd {
        private final String constantName;
        private final Value value;

        public AddConstantTxCmd(String constantName, Value value) {
            this.constantName = constantName;
            this.value = value;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.removePALConstant(constantName);
        }
    }

    class RemoveConstantTxCmd implements TxCmd {
        private final String constantName;
        private final Value oldValue;

        public RemoveConstantTxCmd(String constantName, Value oldValue) {
            this.constantName = constantName;
            this.oldValue = oldValue;
        }

        @Override
        public void revert(MemoryPolicyStore store) {
            store.addPALConstant(constantName, oldValue);
        }
    }

    class NoopTxCmd implements TxCmd {
        public NoopTxCmd() {
        }

        @Override
        public void revert(MemoryPolicyStore store) {

        }
    }
}
