package gov.nist.csd.pm.policy.exceptions;

public class DisconnectedNodeException extends PMException{
    public DisconnectedNodeException(String child, String parent) {
        super("deassigning " + child + " from " + parent + " would make " + child + " a disconnected node");
    }
}
