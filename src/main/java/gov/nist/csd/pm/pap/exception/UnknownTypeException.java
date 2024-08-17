package gov.nist.csd.pm.pap.exception;

public class UnknownTypeException extends PMException {
    public UnknownTypeException(String type) {
        super("unknown node type " + type);
    }
}
