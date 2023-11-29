package gov.nist.csd.pm.policy.model.obligation;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.value.ReturnValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Response implements Serializable {

    private final List<PMLStatement> stmts;
    private final String eventCtxVariable;

    public Response(String eventCtxVariable, List<PMLStatement> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.stmts = Collections.unmodifiableList(new ArrayList<>(stmts));
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

    public Value execute(ExecutionContext executionCtx, Policy policy, EventContext eventCtx) throws PMException {
        executionCtx.scope().local().addVariable(eventCtxVariable, Value.fromObject(eventCtx));

        for (PMLStatement stmt : stmts) {
            Value result = stmt.execute(executionCtx, policy);
            if (result instanceof ReturnValue) {
                break;
            }
        }

        return new VoidValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response response = (Response) o;
        return Objects.equals(stmts, response.stmts) && Objects.equals(
                eventCtxVariable, response.eventCtxVariable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts, eventCtxVariable);
    }
}