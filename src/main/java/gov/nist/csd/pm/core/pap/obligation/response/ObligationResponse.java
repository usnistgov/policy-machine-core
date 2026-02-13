package gov.nist.csd.pm.core.pap.obligation.response;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.epp.EventContext;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObligationResponse implements Serializable {

    private final List<PMLStatement<?>> stmts;
    private final String eventCtxVariable;

    public ObligationResponse(String eventCtxVariable, List<PMLStatement<?>> stmts) {
        this.eventCtxVariable = eventCtxVariable;
        this.stmts = List.copyOf(stmts);
    }

    public String getEventCtxVariable() {
        return eventCtxVariable;
    }

    public List<PMLStatement<?>> getStatements() {
        return stmts;
    }

    public void execute(PAP pap, UserContext author, EventContext evtCtx) throws PMException {
        Args args = new Args();

        FormalParameter<Map<String, Object>> eventCtxParam = new FormalParameter<>(
            eventCtxVariable,
            MapType.of(STRING_TYPE, ANY_TYPE)
        );

        args.put(eventCtxParam, eventCtxToMap(evtCtx));

        ExecutionContext executionCtx = pap.buildExecutionContext(author);
        executionCtx.executeStatements(stmts, args);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObligationResponse that)) {
            return false;
        }
        return Objects.equals(stmts, that.stmts) && Objects.equals(eventCtxVariable,
            that.eventCtxVariable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stmts, eventCtxVariable);
    }

    private Map<String, Object> eventCtxToMap(EventContext eventCtx) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", eventCtx.user().getName());
        map.put("attrs", eventCtx.user().getAttrs());
        map.put("process", eventCtx.user().getProcess());
        map.put("granted", eventCtx.granted());
        map.put("opName", eventCtx.opName());
        map.put("args", eventCtx.args());

        return map;
    }
}