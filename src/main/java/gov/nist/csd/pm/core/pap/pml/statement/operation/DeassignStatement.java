package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.graph.DeassignOp.DEASSIGN_ASCENDANT_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.DeassignOp.DEASSIGN_DESCENDANTS_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.IdNodeArg;
import gov.nist.csd.pm.core.pap.function.arg.NodeArg;
import gov.nist.csd.pm.core.pap.function.op.graph.DeassignOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeassignStatement extends OperationStatement {

    private final Expression<String> ascendant;
    private final Expression<List<String>> deassignFrom;

    public DeassignStatement(Expression<String> ascendant, Expression<List<String>> deassignFrom) {
        super(new DeassignOp());
        this.ascendant = ascendant;
        this.deassignFrom = deassignFrom;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap);
        List<String> descs = deassignFrom.execute(ctx, pap);

        long ascId = pap.query().graph().getNodeId(asc);
        List<NodeArg<?>> descIds = new ArrayList<>();
        for (String desc : descs) {
            descIds.add(new IdNodeArg(pap.query().graph().getNodeId(desc)));
        }

        return new Args()
            .put(DEASSIGN_ASCENDANT_PARAM, new IdNodeArg(ascId))
            .put(DEASSIGN_DESCENDANTS_PARAM, descIds);
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