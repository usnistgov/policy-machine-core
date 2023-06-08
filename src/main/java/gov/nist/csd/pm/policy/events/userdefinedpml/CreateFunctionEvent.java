package gov.nist.csd.pm.policy.events.userdefinedpml;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Objects;

public class CreateFunctionEvent implements PolicyEvent {

    private final FunctionDefinitionStatement functionDefinitionStatement;

    public CreateFunctionEvent(FunctionDefinitionStatement functionDefinitionStatement) {
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
    public void apply(Policy policy) throws PMException {
        policy.userDefinedPML().createFunction(functionDefinitionStatement);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateFunctionEvent that = (CreateFunctionEvent) o;
        return Objects.equals(functionDefinitionStatement, that.functionDefinitionStatement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionDefinitionStatement);
    }
}
