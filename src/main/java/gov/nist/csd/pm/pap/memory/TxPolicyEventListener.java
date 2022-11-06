package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.obligation.Obligation;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;

import java.util.ArrayList;
import java.util.List;

class TxPolicyEventListener implements PolicyEventListener, TxCmd<MemoryPolicyStore> {

    private final List<TxCmd<?>> events;

    public TxPolicyEventListener() {
        events = new ArrayList<>();
    }

    public List<TxCmd<?>> getEvents() {
        return events;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) {
        this.events.add(0, eventToCmd(event));
    }

    private TxCmd<?> eventToCmd(PolicyEvent event) {
        if (event instanceof AddConstantEvent e) {
            return new AddConstantTxCmd(e.getName(), e.getValue());
        } else if (event instanceof AddFunctionEvent e) {
            return new AddFunctionTxCmd(e.getFunctionDefinitionStatement());
        } else if (event instanceof AssignEvent e) {
            return new AssignTxCmd(e.getChild(), e.getParent());
        } else if (event instanceof AssignToEvent e) {
            return new AssignTxCmd(e.getChild(), e.getParent());
        } else if (event instanceof AssociateEvent e) {
            return new AssociateTxCmd(new Association(e.getUa(), e.getTarget(), e.getAccessRightSet()));
        } else if (event instanceof CreateObjectAttributeEvent e) {
            return new CreateObjectAttributeTxCmd(e.getName(), e.getProperties(), e.getInitialParent(), e.getAdditionalParents());
        } else if (event instanceof CreateObjectEvent e) {
            return new CreateObjectTxCmd(e.getName(), e.getProperties(), e.getInitialParent(), e.getAdditionalParents());
        } else if (event instanceof CreateObligationEvent e) {
            return new CreateObligationTxCmd(new Obligation(e.getAuthor(), e.getLabel(), e.getRules()));
        } else if (event instanceof CreatePolicyClassEvent e) {
            return new CreatePolicyClassTxCmd(e.getName(), e.getProperties());
        } else if (event instanceof CreateProhibitionEvent e) {
            return new CreateProhibitionTxCmd(new Prohibition(e.getLabel(), e.getSubject(), e.getAccessRightSet(), e.isIntersection(), e.getContainers()));
        } else if (event instanceof CreateUserAttributeEvent e) {
            return new CreateUserAttributeTxCmd(e.getName(), e.getProperties(), e.getInitialParent(), e.getAdditionalParents());
        } else if (event instanceof CreateUserEvent e) {
            return new CreateUserTxCmd(e.getName(), e.getProperties(), e.getInitialParent(), e.getAdditionalParents());
        } else if (event instanceof DeassignEvent e) {
            return new DeassignTxCmd(e.getChild(), e.getParent());
        } else if (event instanceof TxEvents.MemoryDeleteNodeEvent e) {
            return new DeleteNodeTxCmd(e.getName(), e.getNode(), e.getParents());
        } else if (event instanceof TxEvents.MemoryDeleteObligationEvent e) {
            return new DeleteObligationTxCmd(e.getLabel(), e.getObligationToDelete());
        } else if (event instanceof TxEvents.MemoryDeleteProhibitionEvent e) {
            return new DeleteProhibitionTxCmd(e.getLabel(), e.getProhibition());
        } else if (event instanceof TxEvents.MemoryDissociateEvent e) {
            return new DissociateTxCmd(new Association(e.getUa(), e.getTarget(), e.getAccessRightSet()));
        } else if (event instanceof TxEvents.MemoryRemoveConstantEvent e) {
            return new RemoveConstantTxCmd(e.getName(), e.getValue());
        } else if (event instanceof TxEvents.MemoryRemoveFunctionEvent e) {
            return new RemoveFunctionTxCmd(e.getFunctionDefinitionStatement());
        } else if (event instanceof TxEvents.MemorySetNodePropertiesEvent e) {
            return new SetNodePropertiesTxCmd(e.getName(), e.getOldProps(), e.getProperties());
        } else if (event instanceof TxEvents.MemoryUpdateObligationEvent e) {
            return new UpdateObligationTxCmd(new Obligation(e.getAuthor(), e.getLabel(), e.getRules()), e.getOldObl());
        } else if (event instanceof TxEvents.MemoryUpdateProhibitionEvent e) {
            return new UpdateProhibitionTxCmd(
                    new Prohibition(e.getName(), e.getSubject(), e.getAccessRightSet(), e.isIntersection(), e.getContainers()),
                    e.getOldPro()
            );
        }

        return new NoopTxCmd();
    }

    @Override
    public void apply(MemoryPolicyStore store) throws PMException {
        for (TxCmd<?> cmd : events) {
            if (cmd instanceof GraphTxCmd g) {
                g.apply(store.getGraph());
            } else if (cmd instanceof ProhibitionsTxCmd p) {
                p.apply(store.getProhibitions());
            } else if (cmd instanceof ObligationsTxCmd p) {
                p.apply(store.getObligations());
            } else if (cmd instanceof PALTxCmd p) {
                p.apply(store.getPAL());
            }
        }
    }

    @Override
    public void revert(MemoryPolicyStore store) throws PMException {
        for (TxCmd<?> cmd : events) {
            if (cmd instanceof GraphTxCmd g) {
                g.revert(store.getGraph());
            } else if (cmd instanceof ProhibitionsTxCmd p) {
                p.revert(store.getProhibitions());
            } else if (cmd instanceof ObligationsTxCmd p) {
                p.revert(store.getObligations());
            } else if (cmd instanceof PALTxCmd p) {
                p.revert(store.getPAL());
            }
        }
    }
}
