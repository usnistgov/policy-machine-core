package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.epp.EventContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Response implements Serializable {

    private List<PMLStatement> stmts;
    private String eventCtxVariable;

    public Response() {
        stmts = new ArrayList<>();
        eventCtxVariable = "";
    }

    public Response(String eventCtxVariable, List<PMLStatement> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.stmts = stmts;
    }

    public Response(String eventCtxVariable, PMLStatement... stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.stmts = List.of(stmts);
    }

    public Response(PMLStatement... stmts) {
        this.eventCtxVariable = "";
        this.stmts = List.of(stmts);
    }

    public Response(Response response) {
        this.eventCtxVariable = response.eventCtxVariable;
        this.stmts = response.stmts;
    }

    public List<PMLStatement> getStmts() {
        return stmts;
    }

    public void setStmts(List<PMLStatement> stmts) {
        this.stmts = stmts;
    }

    public void setEventCtxVariable(String eventCtxVariable) {
        this.eventCtxVariable = eventCtxVariable;
    }

    public String getEventCtxVariable() {
        return eventCtxVariable;
    }

    public List<PMLStatement> getStatements() {
        return stmts;
    }

    public Value execute(Policy policyAuthor, EventContext eventCtx) throws PMException {
        ExecutionContext executionCtx = new ExecutionContext(eventCtx.getUserCtx());
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
        return Objects.equals(stmts, response.stmts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts);
    }
}
