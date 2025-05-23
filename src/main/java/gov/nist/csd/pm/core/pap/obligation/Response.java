package gov.nist.csd.pm.core.pap.obligation;

import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Response implements Serializable {

    private final List<PMLStatement<?>> stmts;
    private final String eventCtxVariable;

    public Response(String eventCtxVariable, List<PMLStatement<?>> stmts) {
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

    public List<PMLStatement<?>> getStatements() {
        return stmts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response response)) return false;
        return Objects.equals(stmts, response.stmts) && Objects.equals(eventCtxVariable, response.eventCtxVariable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts, eventCtxVariable);
    }
}