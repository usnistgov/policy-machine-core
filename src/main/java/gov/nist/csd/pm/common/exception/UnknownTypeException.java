package gov.nist.csd.pm.common.exception;

public class UnknownTypeException extends PMException {
    public UnknownTypeException(String type) {
        super("unknown node type " + type);
    }
}
