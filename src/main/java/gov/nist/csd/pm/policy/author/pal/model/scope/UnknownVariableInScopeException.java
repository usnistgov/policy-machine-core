package gov.nist.csd.pm.policy.author.pal.model.scope;

public class UnknownVariableInScopeException extends PALScopeException {

    public UnknownVariableInScopeException(String functionName) {
        super(String.format("unknown variable '%s' in scope", functionName));
    }

}
