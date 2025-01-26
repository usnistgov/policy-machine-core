package gov.nist.csd.pm.pap;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.id.IdGenerator;
import gov.nist.csd.pm.pap.modification.PolicyModification;
import gov.nist.csd.pm.pap.store.PolicyStore;

/**
 * PolicyStore is an abstract class that outlines the expected behavior of a backend implementation.
 */
public class PolicyModifier extends Modifier implements PolicyModification {

    private GraphModifier graphModifier;
    private ProhibitionsModifier prohibitionsModifier;
    private ObligationsModifier obligationsModifier;
    private OperationsModifier operationsModifier;
    private RoutinesModifier routinesModifier;

    public PolicyModifier(PolicyStore store, IdGenerator idGenerator) throws PMException {
        super(store);
        this.graphModifier = new GraphModifier(store, idGenerator);
        this.prohibitionsModifier = new ProhibitionsModifier(store);
        this.obligationsModifier = new ObligationsModifier(store);
        this.operationsModifier = new OperationsModifier(store);
        this.routinesModifier = new RoutinesModifier(store);
    }

    @Override
    public GraphModifier graph() {
        return graphModifier;
    }

    @Override
    public ProhibitionsModifier prohibitions() {
        return prohibitionsModifier;
    }

    @Override
    public ObligationsModifier obligations() {
        return obligationsModifier;
    }

    @Override
    public OperationsModifier operations() {
        return operationsModifier;
    }

    @Override
    public RoutinesModifier routines() {
        return routinesModifier;
    }
}
