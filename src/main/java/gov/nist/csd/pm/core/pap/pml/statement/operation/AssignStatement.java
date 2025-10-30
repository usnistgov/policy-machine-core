package gov.nist.csd.pm.core.pap.pml.statement.operation;

import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.ASCENDANT_PARAM;
import static gov.nist.csd.pm.core.pap.function.op.graph.GraphOp.DESCENDANTS_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.op.graph.AssignOp;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.Expression;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.List;
import java.util.Objects;

public class AssignStatement extends OperationStatement {

    private final Expression<String> ascendant;
    private final Expression<List<String>> descendants;

    public AssignStatement(Expression<String> ascendant, Expression<List<String>> descendants) {
        super(new AssignOp());

        this.ascendant = ascendant;
        this.descendants = descendants;
    }

    @Override
    public Args prepareArgs(ExecutionContext ctx, PAP pap) throws PMException {
        String asc = ascendant.execute(ctx, pap);
        List<String> descs = descendants.execute(ctx, pap);

        // convert to ids
        long ascId = pap.query().graph().getNodeByName(asc).getId();
        LongArrayList descIds = new LongArrayList();
        for (String desc : descs) {
            descIds.add(pap.query().graph().getNodeByName(desc).getId());
        }

        return new Args()
            .put(ASCENDANT_PARAM, ascId)
            .put(DESCENDANTS_PARAM, descIds);
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