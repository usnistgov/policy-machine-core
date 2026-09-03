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
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.ngac.pm.core.pap.pml.statement.result.BreakResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.ReturnResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.StatementResult;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A PML foreach statement that iterates a list or map, binding each entry to a loop variable.
 */
public class ForeachStatement extends BasicStatement<StatementResult> {

    private final String varName;
    private final String valueVarName;
    private final Expression<?> iter;
    private final List<PMLStatement<?>> statements;

    public ForeachStatement(String varName, String valueVarName, Expression<?> iter, List<PMLStatement<?>> statements) {
        this.varName = varName;
        this.valueVarName = valueVarName;
        this.iter = iter;
        this.statements = statements;
    }

    @Override
    public StatementResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        if (statements.isEmpty()) {
            return new VoidResult();
        }

        Type<?> iterType = iter.getType();
        Object iterValue = iter.execute(ctx, pap);
        if (iterValue instanceof List<?> list && iterType instanceof ListType<?> listType) {
            return executeArrayIterator(ctx, list, listType);
        } else if (iterValue instanceof Map<?, ?> map && iterType instanceof MapType<?, ?> mapType) {
            return executeMapIterator(ctx, map, mapType);
        }

        return new VoidResult();
    }

    private StatementResult executeArrayIterator(ExecutionContext ctx, List<?> iterValue, ListType<?> listType) throws PMException{
        for (Object o : iterValue) {
            Args args = new Args();
            args.putUnchecked(new FormalParameter<>(varName, listType.getElementType()), o);

            StatementResult value = ctx.executeStatements(statements, args);

            if (value instanceof BreakResult) {
                break;
            } else if (value instanceof ReturnResult) {
                return value;
            }
        }
        return new VoidResult();
    }

    private StatementResult executeMapIterator(ExecutionContext ctx, Map<?, ?> iterValue, MapType<?, ?> mapType) throws PMException{
        for (Object key : iterValue.keySet()) {
            Object value = iterValue.get(key);

            // add the key value
            Args args = new Args();
            args.putUnchecked(new FormalParameter<>(varName, Type.resolveTypeOfObject(key)), key);

            // add the value value
            if (valueVarName != null) {
                args.putUnchecked(new FormalParameter<>(valueVarName, mapType.getValueType()), value);
            }

            StatementResult result = ctx.executeStatements(statements, args);

            if (result instanceof BreakResult) {
                break;
            } else if (result instanceof ReturnResult) {
                return result;
            }
        }

        return new VoidResult();
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return String.format("%sforeach %s in %s %s",
                indent(indentLevel), (valueVarName != null ? String.format("%s, %s", varName, valueVarName) : varName),
                iter,
                new PMLStatementBlock(statements).toFormattedString(indentLevel)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForeachStatement that)) return false;
        return Objects.equals(varName, that.varName) && Objects.equals(valueVarName, that.valueVarName) && Objects.equals(iter, that.iter) && Objects.equals(statements, that.statements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, valueVarName, iter, statements);
    }
} 