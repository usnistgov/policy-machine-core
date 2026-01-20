package gov.nist.csd.pm.core.pap.pml.scope;

public class UnknownOperationInScopeException extends PMLScopeException {

    public UnknownOperationInScopeException(String functionName) {
        super(String.format("unknown operation '%s' in scope", functionName));
    }

}
