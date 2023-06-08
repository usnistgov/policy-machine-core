package gov.nist.csd.pm.policy.events;

import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddFunctionEvent that = (AddFunctionEvent) o;
        return Objects.equals(functionDefinitionStatement, that.functionDefinitionStatement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionDefinitionStatement);
    }
}
