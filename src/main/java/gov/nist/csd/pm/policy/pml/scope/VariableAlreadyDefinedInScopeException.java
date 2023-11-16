package gov.nist.csd.pm.policy.pml.scope;

public class VariableAlreadyDefinedInScopeException extends PMLScopeException{

    public VariableAlreadyDefinedInScopeException(String varName) {
        super(String.format("variable '%s' already defined in scope", varName));
    }
}
