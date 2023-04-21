package gov.nist.csd.pm.policy.pml;

import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;

public interface PALSerializable {

    void fromPAL(UserContext author, String input, FunctionDefinitionStatement... customFunctions) throws PMException;
    String toPAL(boolean format) throws PMException;

}
