package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.executable.op.graph.AssignOp;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.VoidValue;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssignStatement extends OperationStatement<AssignOp> {

    private final Expression ascendant;
    private final Expression descendants;

    public AssignStatement(Expression ascendant, Expression descendants) {
        super(new AssignOp());

        this.ascendant = ascendant;
        this.descendants = descendants;
    }

    @Override
    public ActualArgs prepareOperands(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap).getStringValue();
        List<Value> assignToValue = descendants.execute(ctx, pap).getArrayValue();
        List<String> descs = new ArrayList<>();
        for (Value value : assignToValue) {
            descs.add(value.getStringValue());
        }

        // convert to ids
        long ascId = pap.query().graph().getNodeByName(asc).getId();
        LongArrayList descIds = new LongArrayList();
        for (String desc : descs) {
            descIds.add(pap.query().graph().getNodeByName(desc).getId());
        }

        return op.actualArgs(ascId, descIds);
    }
    
    @Override
    public Value execute(ExecutionContext ctx, PAP pap) throws PMException {
        ActualArgs actualArgs = prepareOperands(ctx, pap);
        op.execute(pap, actualArgs);
        return new VoidValue();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("assign %s to %s", ascendant, descendants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssignStatement that)) return false;
        return Objects.equals(ascendant, that.ascendant) && Objects.equals(descendants, that.descendants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ascendant, descendants);
    }
} 