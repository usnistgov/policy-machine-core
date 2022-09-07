package gov.nist.csd.pm.policy.author;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.pal.PALContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.model.access.UserContext;

import java.util.Map;

public interface PALAuthor {

    void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException;
    void removeFunction(String functionName) throws PMException;
    Map<String, FunctionDefinitionStatement> getFunctions() throws PMException;

    void addConstant(String constantName, Value constantValue) throws PMException;
    void removeConstant(String constName) throws PMException;
    Map<String, Value> getConstants() throws PMException;

    PALContext getContext() throws PMException;

}
