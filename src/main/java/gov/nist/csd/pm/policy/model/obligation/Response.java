package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.author.pal.model.scope.PALScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.EventSpecificResponseStatement;
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
    public Response(ExecutionContext executionCtx, List<PALStatement> stmts) {
        this.executionCtx = executionCtx;
        this.stmts = stmts;
    }

    public Response(ExecutionContext executionCtx, PALStatement... stmts) {
        this.executionCtx = executionCtx;
        this.stmts = List.of(stmts);
    }

    public Response(UserContext author, PALStatement... stmts) {
        this.executionCtx = new ExecutionContext(author);
        this.stmts = List.of(stmts);
    }

    public Response(Response response) {
        this.executionCtx = response.executionCtx;
        this.stmts = response.stmts;
    }

    public List<PALStatement> getStatements() {
        return stmts;
    }

    public ExecutionContext getExecutionCtx() {
        return executionCtx;
    }

    public Value execute(PolicyAuthor policyAuthor, EventContext eventCtx) throws PMException {
        ExecutionContext copyCtx = null;
        try {
            copyCtx = executionCtx.copy();
        } catch (PALScopeException e) {
            throw new PMException(e.getMessage());
        }

        for (PALStatement stmt : stmts) {
            String eventName = eventCtx.getEventName();

            if (stmt instanceof EventSpecificResponseStatement eventSpecificResponseStmt) {
                if (!eventSpecificResponseStmt.getEvent().equals(eventName)) {
                    continue;
                }

                Value eventCtxValue = Value.objectToValue(eventCtx);
                copyCtx.scope().addValue(eventSpecificResponseStmt.getEvent(), eventCtxValue);

                if (eventSpecificResponseStmt.hasAlias()) {
                    copyCtx.scope().addValue(eventSpecificResponseStmt.getAlias(), eventCtxValue);
                }

                eventSpecificResponseStmt.execute(copyCtx, policyAuthor);
            } else {
                stmt.execute(copyCtx, policyAuthor);
            }
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
