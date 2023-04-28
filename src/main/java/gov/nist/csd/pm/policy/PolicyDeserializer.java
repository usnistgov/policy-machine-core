package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public interface PolicyDeserializer {

    void fromJSON(String json) throws PMException;

    void fromPML(UserContext author, String pml, FunctionDefinitionStatement ... customFunctions) throws PMException;

}
