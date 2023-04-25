package gov.nist.csd.pm.policy.pml.model.scope;

public class UnknownVariableInScopeException extends PMLScopeException {

    public UnknownVariableInScopeException(String functionName) {
        super(String.format("unknown variable '%s' in scope", functionName));
    }

}
