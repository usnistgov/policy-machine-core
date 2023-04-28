package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.epp.EventContext;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Response implements Serializable {

    private final ExecutionContext executionCtx;
    private final List<PMLStatement> stmts;
    private final String eventCtxVariable;

    public Response(String eventCtxVariable, ExecutionContext executionCtx, List<PMLStatement> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.executionCtx = executionCtx;
        this.stmts = stmts;
    }

    public Response(String eventCtxVariable, ExecutionContext executionCtx, PMLStatement... stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.executionCtx = executionCtx;
        this.stmts = List.of(stmts);
    }

    public Response(UserContext author, PMLStatement... stmts) {
        this.eventCtxVariable = "";
        this.executionCtx = new ExecutionContext(author);
        this.stmts = List.of(stmts);
    }

    public Response(UserContext author, String eventCtxVariable, PMLStatement... stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.executionCtx = new ExecutionContext(author);
        this.stmts = List.of(stmts);
    }

    public Response(Response response) {
        this.eventCtxVariable = response.eventCtxVariable;
        this.executionCtx = response.executionCtx;
        this.stmts = response.stmts;
    }

    public String getEventCtxVariable() {
        return eventCtxVariable;
    }

    public List<PMLStatement> getStatements() {
        return stmts;
    }

    public ExecutionContext getExecutionCtx() {
        return executionCtx;
    }

    public Value execute(Policy policyAuthor, EventContext eventCtx) throws PMException {
        executionCtx.scope().putValue(eventCtxVariable, Value.objectToValue(eventCtx));

        for (PMLStatement stmt : stmts) {
            stmt.execute(executionCtx, policyAuthor);
        }

        return new Value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return Objects.equals(executionCtx, response.executionCtx) && Objects.equals(stmts, response.stmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(executionCtx, stmts);
    }
}