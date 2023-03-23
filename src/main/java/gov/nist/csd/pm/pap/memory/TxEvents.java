package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.List;
import java.util.Map;

public class TxEvents {

    private TxEvents() {}

    public static class MemoryDeleteNodeEvent extends DeleteNodeEvent {

        private Node node;
        private List<String> parents;

        public MemoryDeleteNodeEvent(String name, Node node, List<String> parents) {
            super(name);
            this.node = node;
            this.parents = parents;
        }

        public Node getNode() {
            return node;
        }

        public List<String> getParents() {
            return parents;
        }
    }

    public static class MemoryDeleteObligationEvent extends DeleteObligationEvent {

        private Obligation obligationToDelete;

        public MemoryDeleteObligationEvent(Obligation obligationToDelete) {
            super(obligationToDelete);
            this.obligationToDelete = obligationToDelete;
        }

        public Obligation getObligationToDelete() {
            return obligationToDelete;
        }
    }

    public static class MemoryDeleteProhibitionEvent extends DeleteProhibitionEvent {

        public MemoryDeleteProhibitionEvent(Prohibition prohibition) {
            super(prohibition);
        }
    }

    public static class MemoryDissociateEvent extends DissociateEvent {

        private AccessRightSet accessRightSet;

        public MemoryDissociateEvent(String ua, String target, AccessRightSet accessRightSet) {
            super(ua, target);
            this.accessRightSet = accessRightSet;
        }

        public AccessRightSet getAccessRightSet() {
            return accessRightSet;
        }
    }

    public static class MemoryRemoveConstantEvent extends RemoveConstantEvent {

        private Value value;

        public MemoryRemoveConstantEvent(String constantName, Value value) {
            super(constantName);
            this.value = value;
        }

        public Value getValue() {
            return value;
        }
    }

    public static class MemoryRemoveFunctionEvent extends RemoveFunctionEvent {

        private FunctionDefinitionStatement functionDefinitionStatement;

        public MemoryRemoveFunctionEvent(FunctionDefinitionStatement functionDefinitionStatement) {
            super(functionDefinitionStatement.getFunctionName());
            this.functionDefinitionStatement = functionDefinitionStatement;
        }

        public FunctionDefinitionStatement getFunctionDefinitionStatement() {
            return functionDefinitionStatement;
        }
    }

    public static class MemorySetNodePropertiesEvent extends SetNodePropertiesEvent {

        private Map<String, String> oldProps;

        public MemorySetNodePropertiesEvent(String name, Map<String, String> oldProps, Map<String, String> newProps) {
            super(name, newProps);
            this.oldProps = oldProps;
        }

        public Map<String, String> getOldProps() {
            return oldProps;
        }
    }

    public static class MemoryUpdateObligationEvent extends UpdateObligationEvent {

        private Obligation oldObl;

        public MemoryUpdateObligationEvent(Obligation newObl, Obligation oldObl) {
            super(newObl.getAuthor(), newObl.getLabel(), newObl.getRules());
            this.oldObl = oldObl;
        }

        public Obligation getOldObl() {
            return oldObl;
        }
    }

    public static class MemoryUpdateProhibitionEvent extends UpdateProhibitionEvent {

        private Prohibition oldPro;

        public MemoryUpdateProhibitionEvent(Prohibition newPro, Prohibition oldPro) {
            super(newPro.getLabel(), newPro.getSubject(), newPro.getAccessRightSet(), newPro.isIntersection(), newPro.getContainers());
            this.oldPro = oldPro;
        }

        public Prohibition getOldPro() {
            return oldPro;
        }
    }
}
