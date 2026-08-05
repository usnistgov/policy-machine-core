package gov.nist.ngac.pm.core.common.exception;

public class UnknownTypeException extends PMException {
    public UnknownTypeException(String type) {
        super("unknown node type " + type);
    }
}
