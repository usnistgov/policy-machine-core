package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.pap.id.IdGenerator;

/**
 * PolicyStore is an abstract class that outlines the expected behavior of a backend implementation.
 */
public class PolicyModifier implements PolicyModification {

    private final GraphModifier graphModifier;
    private final ProhibitionsModifier prohibitionsModifier;
    private final ObligationsModifier obligationsModifier;
    private final OperationsModifier operationsModifier;
    private final RoutinesModifier routinesModifier;

    public PolicyModifier(GraphModifier graphModifier,
                          ProhibitionsModifier prohibitionsModifier,
                          ObligationsModifier obligationsModifier,
                          OperationsModifier operationsModifier,
                          RoutinesModifier routinesModifier) {
        this.graphModifier = graphModifier;
        this.prohibitionsModifier = prohibitionsModifier;
        this.obligationsModifier = obligationsModifier;
        this.operationsModifier = operationsModifier;
        this.routinesModifier = routinesModifier;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.graphModifier.setIdGenerator(idGenerator);
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
