package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.op.Operation;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GraphOp<T> extends Operation<T> {

    public static final String TYPE_OPERAND = "type";
    public static final String DESCENDANTS_OPERAND = "descendants";
    public static final String PROPERTIES_OPERAND = "properties";
    public static final String ASCENDANT_OPERAND = "ascendant";
    public static final String UA_OPERAND = "ua";
    public static final String TARGET_OPERAND = "target";
    public static final String ARSET_OPERAND = "arset";

    public GraphOp(String name, List<String> allOperands, List<String> nodeOperands) {
        super(name, allOperands, nodeOperands);
    }

    public GraphOp(String name, List<String> allOperands) {
        super(name, allOperands);
    }

    public GraphOp(String name) {
        super(name);
    }

    /**
     * Convert the given operation and operands to an event context to be sent to the EPP. If the operation uses node IDs
     * as operands in execute(), those IDs are changed to the node names. If an operation uses the ID_OPERAND key, the
     * returned event context will change that to the NAME_OPERAND key.
     * @param pap The PAP object.
     * @param userCtx The user who initiated the event.
     * @param operands The operands of the operation.
     * @return The EventContext representing the operation.
     * @throws PMException If there is an exception building the EventContext.
     */
    public abstract EventContext toEventContext(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException;
}
