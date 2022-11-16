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
import java.util.function.Function;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.PC;

interface TxCmd<T> {

    void apply(T t) throws PMException;
    void revert(T t) throws PMException;

    abstract class GraphTxCmd implements TxCmd<MemoryGraphStore> { }
    abstract class ProhibitionsTxCmd implements TxCmd<MemoryProhibitionsStore> { }
    abstract class ObligationsTxCmd implements TxCmd<MemoryObligationsStore> { }
    abstract class PALTxCmd implements TxCmd<MemoryPALStore> { }
    
    class CreatePolicyClassTxCmd extends GraphTxCmd {
            
        private String name;
        private Map<String, String> properties;

        public CreatePolicyClassTxCmd(String name, Map<String, String> properties) {
            this.name = name;
            this.properties = properties;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.createPolicyClass(name, properties);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deleteNode(name);
        }
    }

    class CreateObjectAttributeTxCmd extends GraphTxCmd {
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
        public void apply(MemoryGraphStore store) {
            store.createObjectAttribute(name, properties, parent, parents);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deleteNode(name);
        }
    }

    class CreateUserAttributeTxCmd extends GraphTxCmd {
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
        public void apply(MemoryGraphStore store) {
            store.createUserAttribute(name, properties, parent, parents);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deleteNode(name);
        }
    }

    class CreateObjectTxCmd extends GraphTxCmd {
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
        public void apply(MemoryGraphStore store) {
            store.createObject(name, properties, parent, parents);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deleteNode(name);
        }
    }

    class CreateUserTxCmd extends GraphTxCmd {
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
        public void apply(MemoryGraphStore store) {
            store.createUser(name, properties, parent, parents);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deleteNode(name);
        }
    }

    class SetNodePropertiesTxCmd extends GraphTxCmd {
        private final String name;
        private final Map<String, String> oldProperties;
        private final Map<String, String> newProperties;

        public SetNodePropertiesTxCmd(String name, Map<String, String> oldProperties, Map<String, String> newProperties) {
            this.name = name;
            this.oldProperties = oldProperties;
            this.newProperties = newProperties;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.setNodeProperties(name, newProperties);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.setNodeProperties(name, oldProperties);
        }
    }

    class DeleteNodeTxCmd extends GraphTxCmd {
        private final String name;
        private final Node nodeToDelete;
        private final List<String> parents;

        public DeleteNodeTxCmd(String name, Node nodeToDelete, List<String> parents) {
            this.name = name;
            this.nodeToDelete = nodeToDelete;
            this.parents = parents;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.deleteNode(name);
        }

        @Override
        public void revert(MemoryGraphStore store) {
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

    final class AssignTxCmd extends GraphTxCmd {
        private final String child;
        private final String parent;

        public AssignTxCmd(String child, String parent) {
            this.child = child;
            this.parent = parent;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.assign(child, parent);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.deassign(child, parent);
        }
    }

    class DeassignTxCmd extends GraphTxCmd {
        private final String child;
        private final String parent;

        public DeassignTxCmd(String child, String parent) {
            this.child = child;
            this.parent = parent;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.deassign(child, parent);
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.assign(child, parent);
        }
    }

    class AssociateTxCmd extends GraphTxCmd {
        private final Association association;

        public AssociateTxCmd(Association association) {
            this.association = association;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.associate(association.getSource(), association.getTarget(), association.getAccessRightSet());
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.dissociate(association.getSource(), association.getTarget());
        }
    }

    class DissociateTxCmd extends GraphTxCmd {
        private final Association association;

        public DissociateTxCmd(Association association) {
            this.association = association;
        }

        @Override
        public void apply(MemoryGraphStore store) {
            store.dissociate(association.getSource(), association.getTarget());
        }

        @Override
        public void revert(MemoryGraphStore store) {
            store.associate(association.getSource(), association.getTarget(), association.getAccessRightSet());
        }
    }

    class CreateProhibitionTxCmd extends ProhibitionsTxCmd {
        private final Prohibition prohibition;

        public CreateProhibitionTxCmd(Prohibition prohibition) {
            this.prohibition = prohibition;
        }

        @Override
        public void apply(MemoryProhibitionsStore store) {
            store.create(
                    prohibition.getLabel(),
                    prohibition.getSubject(),
                    prohibition.getAccessRightSet(),
                    prohibition.isIntersection(),
                    prohibition.getContainers().toArray(new ContainerCondition[]{})
            );
        }

        @Override
        public void revert(MemoryProhibitionsStore store) {
            store.delete(prohibition.getLabel());
        }
    }

    class UpdateProhibitionTxCmd extends ProhibitionsTxCmd {
        private final Prohibition newProhibition;
        private final Prohibition oldProhibition;

        public UpdateProhibitionTxCmd(Prohibition newProhibition, Prohibition oldProhibition) {
            this.newProhibition = newProhibition;
            this.oldProhibition = oldProhibition;
        }

        @Override
        public void apply(MemoryProhibitionsStore store) throws PMException {
            store.update(
                    newProhibition.getLabel(),
                    newProhibition.getSubject(),
                    newProhibition.getAccessRightSet(),
                    newProhibition.isIntersection(),
                    newProhibition.getContainers().toArray(new ContainerCondition[]{})
            );
        }

        @Override
        public void revert(MemoryProhibitionsStore store) throws PMException {
            store.update(
                    oldProhibition.getLabel(),
                    oldProhibition.getSubject(),
                    oldProhibition.getAccessRightSet(),
                    oldProhibition.isIntersection(),
                    oldProhibition.getContainers().toArray(new ContainerCondition[]{})
            );
        }
    }

    class DeleteProhibitionTxCmd extends ProhibitionsTxCmd {
        private final String label;
        private final Prohibition prohibitionToDelete;

        public DeleteProhibitionTxCmd(String label, Prohibition prohibitionToDelete) {
            this.label = label;
            this.prohibitionToDelete = prohibitionToDelete;
        }

        @Override
        public void apply(MemoryProhibitionsStore store) {
            store.delete(label);
        }

        @Override
        public void revert(MemoryProhibitionsStore store) {
            store.create(
                    prohibitionToDelete.getLabel(),
                    prohibitionToDelete.getSubject(),
                    prohibitionToDelete.getAccessRightSet(),
                    prohibitionToDelete.isIntersection(),
                    prohibitionToDelete.getContainers().toArray(new ContainerCondition[]{})
            );
        }
    }

    class CreateObligationTxCmd extends ObligationsTxCmd {
        private final Obligation obligation;

        public CreateObligationTxCmd(Obligation obligation) {
            this.obligation = obligation;
        }

        @Override
        public void apply(MemoryObligationsStore store) {
            store.create(
                    obligation.getAuthor(),
                    obligation.getLabel(),
                    obligation.getRules().toArray(new Rule[]{})
            );
        }

        @Override
        public void revert(MemoryObligationsStore store) {
            store.delete(obligation.getLabel());
        }
    }

    class UpdateObligationTxCmd extends ObligationsTxCmd {
        private final Obligation newObligation;
        private final Obligation oldObligation;

        public UpdateObligationTxCmd(Obligation newObligation, Obligation oldObligation) {
            this.newObligation = newObligation;
            this.oldObligation = oldObligation;
        }

        @Override
        public void apply(MemoryObligationsStore store) {
            store.update(
                    newObligation.getAuthor(),
                    newObligation.getLabel(),
                    newObligation.getRules().toArray(new Rule[]{})
            );
        }

        @Override
        public void revert(MemoryObligationsStore store) {
            store.update(
                    oldObligation.getAuthor(),
                    oldObligation.getLabel(),
                    oldObligation.getRules().toArray(new Rule[]{})
            );
        }
    }

    class DeleteObligationTxCmd extends ObligationsTxCmd {
        private final String name;
        private final Obligation obligationToDelete;

        public DeleteObligationTxCmd(String name, Obligation obligationToDelete) {
            this.name = name;
            this.obligationToDelete = obligationToDelete;
        }

        @Override
        public void apply(MemoryObligationsStore store) {
            store.delete(name);
        }

        @Override
        public void revert(MemoryObligationsStore store) {
            store.create(
                    obligationToDelete.getAuthor(),
                    obligationToDelete.getLabel(),
                    obligationToDelete.getRules().toArray(new Rule[]{})
            );
        }
    }

    class AddFunctionTxCmd extends PALTxCmd {
        private final FunctionDefinitionStatement functionDefinitionStatement;

        public AddFunctionTxCmd(FunctionDefinitionStatement functionDefinitionStatement) {
            this.functionDefinitionStatement = functionDefinitionStatement;
        }

        @Override
        public void apply(MemoryPALStore store) {
            store.addFunction(functionDefinitionStatement);
        }

        @Override
        public void revert(MemoryPALStore store) {
            store.removeFunction(functionDefinitionStatement.getFunctionName());
        }
    }

    class RemoveFunctionTxCmd extends PALTxCmd {
        private final FunctionDefinitionStatement functionDefinitionStatement;

        public RemoveFunctionTxCmd(FunctionDefinitionStatement functionDefinitionStatement) {
            this.functionDefinitionStatement = functionDefinitionStatement;
        }

        @Override
        public void apply(MemoryPALStore store) {
            store.removeFunction(functionDefinitionStatement.getFunctionName());
        }

        @Override
        public void revert(MemoryPALStore store) {
            store.addFunction(functionDefinitionStatement);
        }
        
    }

    class AddConstantTxCmd extends PALTxCmd {
        private final String constantName;
        private final Value value;

        public AddConstantTxCmd(String constantName, Value value) {
            this.constantName = constantName;
            this.value = value;
        }

        @Override
        public void apply(MemoryPALStore store) {
            store.addConstant(constantName, value);
        }

        @Override
        public void revert(MemoryPALStore store) {
            store.removeConstant(constantName);
        }
    }

    class RemoveConstantTxCmd extends PALTxCmd {
        private final String constantName;
        private final Value oldValue;

        public RemoveConstantTxCmd(String constantName, Value oldValue) {
            this.constantName = constantName;
            this.oldValue = oldValue;
        }

        @Override
        public void apply(MemoryPALStore store) {
            store.removeConstant(constantName);
        }

        @Override
        public void revert(MemoryPALStore store) {
            store.addConstant(constantName, oldValue);
        }
    }

    class NoopTxCmd extends GraphTxCmd {
        public NoopTxCmd() {
        }
        
        @Override
        public void apply(MemoryGraphStore store) {
            
        }

        @Override
        public void revert(MemoryGraphStore store) {

        }
    }
}
