package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

public class AddFunctionEvent implements PolicyEvent {

    private final FunctionDefinitionStatement functionDefinitionStatement;

    public AddFunctionEvent(FunctionDefinitionStatement functionDefinitionStatement) {
        this.functionDefinitionStatement = functionDefinitionStatement;
    }

    public FunctionDefinitionStatement getFunctionDefinitionStatement() {
        return functionDefinitionStatement;
    }

    @Override
    public String getEventName() {
        return "add_function";
    }
}
