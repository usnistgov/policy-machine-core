package gov.nist.csd.pm.pap.pml.scope;

public class ExecutableAlreadyDefinedInScopeException extends PMLScopeException {

    public ExecutableAlreadyDefinedInScopeException(String funcName) {
        super(String.format("executable '%s' already defined in scope", funcName));
    }

}
