package gov.nist.csd.pm.common.op.graph;

import gov.nist.csd.pm.common.op.Operation;

import java.util.List;

public abstract class GraphOp extends Operation<Void> {

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
}
