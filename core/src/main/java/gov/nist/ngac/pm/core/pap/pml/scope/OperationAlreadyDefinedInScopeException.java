package gov.nist.ngac.pm.core.pap.pml.scope;

public class OperationAlreadyDefinedInScopeException extends PMLScopeException {

    public OperationAlreadyDefinedInScopeException(String funcName) {
        super(String.format("operation '%s' already defined in scope", funcName));
    }

}
