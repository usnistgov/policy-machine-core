package gov.nist.csd.pm.core.pap.pml.scope;

public class FunctionAlreadyDefinedInScopeException extends PMLScopeException {

    public FunctionAlreadyDefinedInScopeException(String funcName) {
        super(String.format("function '%s' already defined in scope", funcName));
    }

}
