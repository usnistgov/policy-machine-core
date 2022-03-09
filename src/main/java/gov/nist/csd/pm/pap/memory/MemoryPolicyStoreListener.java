package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.pap.store.*;
import gov.nist.csd.pm.policy.events.*;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.obligation.Rule;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;

public class MemoryPolicyStoreListener extends PolicyStore implements PolicyEventListener {

    public MemoryPolicyStore store;

    public MemoryPolicyStoreListener(MemoryPolicyStore store) {
        this.store = store;
    }

    @Override
    public void handlePolicyEvent(PolicyEvent event) throws PMException {
            if (event instanceof PolicySynchronizationEvent policySynchronizationEvent) {
                this.store = new MemoryPolicyStore(policySynchronizationEvent);
            } else if (event instanceof CreateNodeEvent createNodeEvent) {
                handleCreateNodeEvent(createNodeEvent);
            } else if (event instanceof AssignEvent assignEvent) {
                handleAssignEvent(assignEvent);
            } else if (event instanceof AssociateEvent associateEvent) {
                handleAssociateEvent(associateEvent);
            } else if (event instanceof CreateObligationEvent createObligationEvent) {
                handleCreateObligationEvent(createObligationEvent);
            } else if (event instanceof CreateProhibitionEvent createProhibitionEvent) {
                handleCreateProhibitionEvent(createProhibitionEvent);
            } else if (event instanceof DeassignEvent deassignEvent) {
                handleDeassignEvent(deassignEvent);
            } else if (event instanceof DeleteNodeEvent deleteNodeEvent) {
                handleDeleteNodeEvent(deleteNodeEvent);
            } else if (event instanceof DeleteObligationEvent deleteObligationEvent) {
                handleDeleteObligationEvent(deleteObligationEvent);
            } else if (event instanceof DeleteProhibitionEvent deleteProhibitionEvent) {
                handleDeleteProhibitionEvent(deleteProhibitionEvent);
            } else if (event instanceof DissociateEvent dissociateEvent) {
                handleDissociateEvent(dissociateEvent);
            } else if (event instanceof SetNodePropertiesEvent setNodePropertiesEvent) {
                handleSetNodePropertiesEvent(setNodePropertiesEvent);
            } else if (event instanceof SetResourceAccessRightsEvent setResourceAccessRightsEvent) {
                handleSetResourceAccessRights(setResourceAccessRightsEvent);
            } else if (event instanceof UpdateObligationEvent updateObligationEvent) {
                handleUpdateObligationEvent(updateObligationEvent);
            } else if (event instanceof UpdateProhibitionEvent updateProhibitionEvent) {
                handleUpdateProhibitionEvent(updateProhibitionEvent);
            } else if (event instanceof BeginTxEvent) {
                store.beginTx();
            } else if (event instanceof CommitTxEvent) {
                store.commit();
            } else if (event instanceof RollbackTxEvent) {
                store.rollback();
            }
    }

    private void handleUpdateProhibitionEvent(UpdateProhibitionEvent updateProhibitionEvent) throws PMException {
        store.prohibitions().update(
                updateProhibitionEvent.getName(),
                updateProhibitionEvent.getSubject(),
                updateProhibitionEvent.getAccessRightSet(),
                updateProhibitionEvent.isIntersection(),
                updateProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    private void handleUpdateObligationEvent(UpdateObligationEvent updateObligationEvent) throws PMException {
        store.obligations().update(
                updateObligationEvent.getAuthor(),
                updateObligationEvent.getLabel(),
                updateObligationEvent.getRules().toArray(Rule[]::new)
        );
    }

    private void handleSetResourceAccessRights(SetResourceAccessRightsEvent setResourceAccessRightsEvent) throws PMException {
        store.graph().setResourceAccessRights(setResourceAccessRightsEvent.getAccessRightSet());
    }

    private void handleSetNodePropertiesEvent(SetNodePropertiesEvent setNodePropertiesEvent) throws PMException {
        store.graph().setNodeProperties(setNodePropertiesEvent.getName(), setNodePropertiesEvent.getProperties());
    }

    private void handleDissociateEvent(DissociateEvent dissociateEvent) throws PMException {
        store.graph().dissociate(dissociateEvent.getUa(), dissociateEvent.getTarget());
    }

    private void handleDeleteProhibitionEvent(DeleteProhibitionEvent deleteProhibitionEvent) throws PMException {
        store.prohibitions().delete(deleteProhibitionEvent.getLabel());
    }

    private void handleDeleteObligationEvent(DeleteObligationEvent deleteObligationEvent) throws PMException {
        store.obligations().delete(deleteObligationEvent.getLabel());
    }

    private void handleDeleteNodeEvent(DeleteNodeEvent deleteNodeEvent) throws PMException {
        store.graph().deleteNode(deleteNodeEvent.getName());
    }

    private void handleDeassignEvent(DeassignEvent deassignEvent) throws PMException {
        store.graph().deassign(deassignEvent.getChild(), deassignEvent.getParent());
    }

    private void handleCreateProhibitionEvent(CreateProhibitionEvent createProhibitionEvent) throws PMException {
        store.prohibitions().create(
                createProhibitionEvent.getLabel(),
                createProhibitionEvent.getSubject(),
                createProhibitionEvent.getAccessRightSet(),
                createProhibitionEvent.isIntersection(),
                createProhibitionEvent.getContainers().toArray(ContainerCondition[]::new)
        );
    }

    private void handleCreateObligationEvent(CreateObligationEvent createObligationEvent) throws PMException {
        store.obligations().create(createObligationEvent.getAuthor(),
                createObligationEvent.getLabel(),
                createObligationEvent.getRules().toArray(Rule[]::new));
    }

    private void handleAssociateEvent(AssociateEvent associateEvent) throws PMException {
        store.graph().associate(associateEvent.getUa(), associateEvent.getTarget(), associateEvent.getAccessRightSet());
    }

    private void handleAssignEvent(AssignEvent assignEvent) throws PMException {
        store.graph().assign(assignEvent.getChild(), assignEvent.getParent());
    }

    private void handleCreateNodeEvent(CreateNodeEvent createNodeEvent) throws PMException {
        switch (createNodeEvent.getType()) {
            case PC -> this.store.graph().createPolicyClass(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties()
            );
            case OA -> this.store.graph().createObjectAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case UA -> this.store.graph().createUserAttribute(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case O -> this.store.graph().createObject(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
            case U -> this.store.graph().createUser(
                    createNodeEvent.getName(),
                    createNodeEvent.getProperties(),
                    createNodeEvent.getInitialParent(),
                    createNodeEvent.getAdditionalParents()
            );
        }
    }

    @Override
    public GraphStore graph() {
        return store.graph();
    }

    @Override
    public ProhibitionsStore prohibitions() {
        return store.prohibitions();
    }

    @Override
    public ObligationsStore obligations() {
        return store.obligations();
    }

    @Override
    public PALStore pal() {
        return store.pal();
    }

    @Override
    public PolicySynchronizationEvent policySync() throws PMException {
        return store.policySync();
    }

    @Override
    public void beginTx() throws PMException {
        store.beginTx();
    }

    @Override
    public void commit() throws PMException {
        store.commit();
    }

    @Override
    public void rollback() throws PMException {
        store.rollback();
    }
}
