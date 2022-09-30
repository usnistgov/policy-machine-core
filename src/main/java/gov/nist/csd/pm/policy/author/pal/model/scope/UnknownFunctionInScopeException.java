package gov.nist.csd.pm.policy.author.pal.model.scope;

public class UnknownFunctionInScopeException extends PALScopeException {

    public UnknownFunctionInScopeException(String functionName) {
        super(String.format("unknown function '%s' in scope", functionName));
    }

}
