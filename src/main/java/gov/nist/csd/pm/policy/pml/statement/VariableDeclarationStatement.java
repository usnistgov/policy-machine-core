package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;

import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.VoidValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VariableDeclarationStatement extends PMLStatement{

    private boolean isConst;
    private List<Declaration> declarations;

    public VariableDeclarationStatement(boolean isConst, List<Declaration> declarations) {
        this.isConst = isConst;
        this.declarations = declarations;
    }

    public VariableDeclarationStatement(boolean isConst, Declaration declarations) {
        this.isConst = isConst;
        this.declarations = new ArrayList<>(List.of(declarations));
    }

    public List<Declaration> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(
            List<Declaration> declarations) {
        this.declarations = declarations;
    }

    public boolean isConst() {
        return isConst;
    }

    public void setConst(boolean aConst) {
        isConst = aConst;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        for (Declaration declaration : declarations) {
            Value value = declaration.expression.execute(ctx, policy);

            if (isConst || !ctx.scope().valueExists(declaration.id)) {
                ctx.scope().addValue(declaration.id, value);
            } else {
                ctx.scope().updateValue(declaration.id, value);
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
        VariableDeclarationStatement that = (VariableDeclarationStatement) o;
        return isConst == that.isConst && Objects.equals(declarations, that.declarations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isConst, declarations);
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String indent = indent(indentLevel);
        StringBuilder sb = new StringBuilder();
        if (isConst) {
            sb.append(indent).append("const (\n");
        } else {
            sb.append(indent).append("var (\n");
        }

        for (Declaration declaration : declarations) {
            sb.append(indent(indentLevel+1)).append(declaration.id).append(" = ").append(declaration.expression).append("\n");
        }

        sb.append(indent).append(")");

        return sb.toString();
    }

    public record Declaration(String id, Expression expression) {

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
