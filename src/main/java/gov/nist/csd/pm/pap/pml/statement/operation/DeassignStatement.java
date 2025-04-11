package gov.nist.csd.pm.pap.pml.statement.operation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.graph.DeassignOp;
import gov.nist.csd.pm.pap.function.op.graph.DeassignOp.DeassignOpArgs;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeassignStatement extends OperationStatement<DeassignOpArgs> {

    private final Expression<String> ascendant;
    private final Expression<List<String>> deassignFrom;

    public DeassignStatement(Expression<String> ascendant, Expression<List<String>> deassignFrom) {
        super(new DeassignOp());
        this.ascendant = ascendant;
        this.deassignFrom = deassignFrom;
    }

    @Override
    public DeassignOpArgs prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap);
        List<String> descs = deassignFrom.execute(ctx, pap);

        long ascId = pap.query().graph().getNodeId(asc);
        LongArrayList descIds = new LongArrayList();
        for (String desc : descs) {
            descIds.add(pap.query().graph().getNodeId(desc));
        }

        return new DeassignOpArgs(ascId, descIds);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return indent(indentLevel) + String.format("deassign %s from %s", ascendant, deassignFrom);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeassignStatement that)) return false;
        return Objects.equals(ascendant, that.ascendant) && Objects.equals(deassignFrom, that.deassignFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ascendant, deassignFrom);
    }
} 