package gov.nist.csd.pm.pap.pml.scope;

public class UnknownFunctionInScopeException extends PMLScopeException {

    public UnknownFunctionInScopeException(String functionName) {
        super(String.format("unknown function '%s' in scope", functionName));
    }

}
