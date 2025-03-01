package gov.nist.csd.pm.pap.pml.scope;

public class UnknownExecutableInScopeException extends PMLScopeException {

    public UnknownExecutableInScopeException(String functionName) {
        super(String.format("unknown function '%s' in scope", functionName));
    }

}
