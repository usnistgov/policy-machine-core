package gov.nist.csd.pm.policy.pml.model.scope;

public class UnknownVariableInScopeException extends PMLScopeException {

    public UnknownVariableInScopeException(String name) {
        super(String.format("unknown variable '%s' in scope", name));
    }

}
