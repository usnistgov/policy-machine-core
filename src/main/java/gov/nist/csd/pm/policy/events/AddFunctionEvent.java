package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.author.pal.statement.FunctionDefinitionStatement;

public class AddFunctionEvent extends PolicyEvent {

    private final FunctionDefinitionStatement functionDefinitionStatement;

    public AddFunctionEvent(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functionDefinitionStatement = functionDefinitionStatement;
    }

    public FunctionDefinitionStatement getFunctionDefinitionStatement() {
        return functionDefinitionStatement;
    }
}
