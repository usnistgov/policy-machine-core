package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.author.PolicyAuthor;
import gov.nist.csd.pm.policy.events.EventContext;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Response implements Serializable {

    private final ExecutionContext executionCtx;
    private final List<PALStatement> stmts;
    private final String eventCtxVariable;
    public Response(String eventCtxVariable, ExecutionContext executionCtx, List<PALStatement> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.executionCtx = executionCtx;
        this.stmts = stmts;
    }

    public Response(String eventCtxVariable, ExecutionContext executionCtx, PALStatement... stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.executionCtx = executionCtx;
        this.stmts = List.of(stmts);
    }

    public Response(UserContext author, PALStatement... stmts) {
        this.eventCtxVariable = "";
        this.executionCtx = new ExecutionContext(author);
        this.stmts = List.of(stmts);
    }

    public Response(Response response) {
        this.executionCtx = response.executionCtx;
        this.stmts = response.stmts;
        this.eventCtxVariable = response.eventCtxVariable;
    }

    public List<PALStatement> getStatements() {
        return stmts;
    }

    public ExecutionContext getExecutionCtx() {
        return executionCtx;
    }

    public String getEventCtxVariable() {
        return eventCtxVariable;
    }

    public Value execute(PolicyAuthor policyAuthor, EventContext eventCtx) throws PMException {
        executionCtx.addVariable(eventCtxVariable, Value.objectToValue(eventCtx), false);

        for (PALStatement stmt : stmts) {
            stmt.execute(executionCtx, policyAuthor);
        }

        return new Value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(executionCtx, response.executionCtx) && Objects.equals(stmts, response.stmts) && Objects.equals(eventCtxVariable, response.eventCtxVariable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionCtx, stmts, eventCtxVariable);
    }
}
