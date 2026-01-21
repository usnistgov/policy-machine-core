package gov.nist.csd.pm.core.pap.pml.scope;

public class UnknownOperationInScopeException extends PMLScopeException {

    public UnknownOperationInScopeException(String operationName) {
        super(String.format("unknown operation '%s' in scope", operationName));
    }

}
