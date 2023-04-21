package gov.nist.csd.pm.policy.pml.model.scope;

public class UnknownFunctionInScopeException extends PALScopeException {

    public UnknownFunctionInScopeException(String functionName) {
        super(String.format("unknown function '%s' in scope", functionName));
    }

}
