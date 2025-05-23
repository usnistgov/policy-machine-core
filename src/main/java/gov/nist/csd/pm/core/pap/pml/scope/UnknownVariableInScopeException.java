package gov.nist.csd.pm.core.pap.pml.scope;

public class UnknownVariableInScopeException extends PMLScopeException {

    public UnknownVariableInScopeException(String name) {
        super(String.format("unknown variable '%s' in scope", name));
    }

}
