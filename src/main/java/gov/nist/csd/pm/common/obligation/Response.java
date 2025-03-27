package gov.nist.csd.pm.common.obligation;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;

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
        ActualArgs actualArgs = new ActualArgs();
        actualArgs.put(new PMLFormalArg(eventCtxVariable, Type.map(Type.string(), Type.any())), Value.fromObject(eventCtx));

        executionCtx.executeStatements(stmts, actualArgs);

        return new VoidValue();
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