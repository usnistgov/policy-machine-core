package gov.nist.csd.pm.pap.exception;

import gov.nist.csd.pm.pap.graph.node.NodeType;

public class DisconnectedNodeException extends PMException {
    public DisconnectedNodeException(String ascendant, String descendant) {
        super("deassigning " + ascendant + " from " + descendant + " would make " + ascendant + " a disconnected node");
    }
    public DisconnectedNodeException(String node, NodeType type) {
        super(node + " is of type " + type + " which is required to be assigned to at least one node initially");
    }
}
