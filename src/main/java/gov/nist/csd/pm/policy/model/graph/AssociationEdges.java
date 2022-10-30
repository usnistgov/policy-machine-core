package gov.nist.csd.pm.policy.model.graph;

import gov.nist.csd.pm.policy.model.graph.relationships.Association;

import java.util.ArrayList;
import java.util.List;

public class AssociationEdges {

    private final List<Association> outgoing;
    private final List<Association> incoming;

    public AssociationEdges(List<Association> outgoing, List<Association> incoming) {
        this.outgoing = outgoing;
        this.incoming = incoming;
    }

    public AssociationEdges(AssociationEdges associations) {
        this.outgoing = new ArrayList<>(associations.outgoing);
        this.incoming = new ArrayList<>(associations.incoming);
    }

    public AssociationEdges() {
        this.outgoing = new ArrayList<>();
        this.incoming = new ArrayList<>();
    }

    public List<Association> outgoing() {
        return new ArrayList<>(outgoing);
    }

    public List<Association> incoming() {
        return new ArrayList<>(incoming);
    }

    public void addOutgoing(Association association) {
        outgoing.add(association);
    }

    public void addIncoming(Association association) {
        incoming.add(association);
    }

    public void removeOutgoing(Association association) {
        outgoing.remove(association);
    }

    public void removeIncoming(Association association) {
        incoming.remove(association);
    }

}
