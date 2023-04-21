package gov.nist.csd.pm.policy.pml.model.scope;

public class FunctionAlreadyDefinedInScopeException extends PALScopeException {

    public FunctionAlreadyDefinedInScopeException(String funcName) {
        super(String.format("function '%s' already defined in scope", funcName));
    }

}
