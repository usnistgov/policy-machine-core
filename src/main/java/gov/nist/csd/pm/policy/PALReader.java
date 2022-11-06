package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.Map;

public interface PALReader {

    Map<String, FunctionDefinitionStatement> getFunctions() throws PMException;
    Map<String, Value> getConstants() throws PMException;
    PALContext getContext() throws PMException;

}
