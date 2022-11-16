package gov.nist.csd.pm.policy;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

public interface PALWriter {

    void addFunction(FunctionDefinitionStatement functionDefinitionStatement) throws PMException;
    void removeFunction(String functionName) throws PMException;
    void addConstant(String constantName, Value constantValue) throws PMException;
    void removeConstant(String constName) throws PMException;

}
