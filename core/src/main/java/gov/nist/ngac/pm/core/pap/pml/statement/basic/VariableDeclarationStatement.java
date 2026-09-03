/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement.basic;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import java.util.List;
import java.util.Objects;

/**
 * A PML statement that declares one or more variables, each initialized to an expression's value.
 */
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

    /**
     * A single variable name paired with its initializing expression.
     *
     * @param id the variable name
     * @param expression the initializing expression
     */
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