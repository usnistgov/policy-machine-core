package gov.nist.csd.pm.pap.pml.statement.basic;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.Expression;
import gov.nist.csd.pm.pap.pml.statement.result.VoidResult;

import java.util.List;
import java.util.Objects;

public class VariableDeclarationStatement extends BasicStatement<VoidResult> {

    private final List<Declaration> declarations;

    public VariableDeclarationStatement(List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        for (Declaration declaration : declarations) {
            Object value = declaration.expression().execute(ctx, pap);
            ctx.scope().updateVariable(declaration.id(), value);
        }

        return new VoidResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableDeclarationStatement that = (VariableDeclarationStatement) o;
        return Objects.equals(declarations, that.declarations);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(declarations);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String indent = indent(indentLevel);
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("var (\n");

        for (Declaration declaration : declarations) {
            sb.append(indent(indentLevel+1)).append(declaration.id()).append(" = ").append(declaration.expression()).append("\n");
        }

        sb.append(indent).append(")");

        return sb.toString();
    }

    public record Declaration(String id, Expression<?> expression) {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Declaration that = (Declaration) o;
            return Objects.equals(id, that.id) && Objects.equals(expression, that.expression);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, expression);
        }
    }
} 