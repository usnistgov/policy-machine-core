package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.Expression;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.VoidValue;
import org.antlr.v4.runtime.ParserRuleContext;

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

    public VariableDeclarationStatement(ParserRuleContext ctx) {
        super(ctx);
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

            ctx.scope().local().addOrOverwriteVariable(declaration.id, value);

            if (isConst) {
                policy.userDefinedPML().createConstant(declaration.id, value);
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

    public static class Declaration {

        private final String id;
        private final Expression expression;

        public Declaration(String id, Expression expression) {
            this.id = id;
            this.expression = expression;
        }

        public String id() {
            return id;
        }

        public Expression expression() {
            return expression;
        }

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
