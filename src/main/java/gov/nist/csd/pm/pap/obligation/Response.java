package gov.nist.csd.pm.pap.obligation;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Response implements Serializable {

    private final List<PMLStatement> stmts;
    private final String eventCtxVariable;

    public Response(String eventCtxVariable, List<PMLStatement> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.stmts = List.copyOf(stmts);
    }

    public Response(Response response) {
        this.eventCtxVariable = response.eventCtxVariable;
        this.stmts = response.stmts;
    }

    public String getEventCtxVariable() {
        return eventCtxVariable;
    }

    public List<PMLStatement> getStatements() {
        return stmts;
    }

    public Value execute(ExecutionContext executionCtx, EventContext eventCtx) throws PMException {
        executionCtx.executeStatements(stmts, Map.of(eventCtxVariable, eventCtx));

        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response)) return false;
        Response response = (Response) o;
        return Objects.equals(stmts, response.stmts) && Objects.equals(eventCtxVariable, response.eventCtxVariable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts, eventCtxVariable);
    }
}