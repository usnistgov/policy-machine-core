package gov.nist.csd.pm.policy.exceptions;

public class UnknownTypeException extends PMException{
    public UnknownTypeException(String type) {
        super("unknown node type " + type);
    }
}
