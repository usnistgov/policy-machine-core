package gov.nist.csd.pm.policy.author.pal.model.scope;

public class VariableAlreadyDefinedInScopeException extends PALScopeException{

    public VariableAlreadyDefinedInScopeException(String var) {
        super(String.format("variable '%s' already defined in scope", var));
    }
}
