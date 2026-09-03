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

package gov.nist.ngac.pm.core.pap.pml.statement.operation;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.Expression;
import gov.nist.ngac.pm.core.pap.pml.statement.result.VoidResult;
import java.util.Objects;

/**
 * Base class for PML delete statements. When the if exists clause is present and the named entity
 * does not exist, execution is skipped instead of failing.
 */
public abstract class DeleteStatement extends OperationStatement {

    protected Type type;
    protected Expression<String> nameExpression;
    protected boolean ifExists;

    public DeleteStatement(Operation<?> op, Type type, Expression<String> nameExpression, boolean ifExists) {
        super(op);
        this.type = type;
        this.nameExpression = nameExpression;
        this.ifExists = ifExists;
    }

    /**
     * Checks whether the named entity currently exists.
     *
     * @param pap the PAP to check against
     * @param name the entity name to check
     * @return whether the entity exists
     * @throws PMException if the existence check fails
     */
    public abstract boolean exists(PAP pap, String name) throws PMException;

    @Override
    public VoidResult execute(ExecutionContext ctx, PAP pap) throws PMException {
        String name = nameExpression.execute(ctx, pap);

        // if the statement includes "if exists" and the entity does not exist, return early
        if (ifExists && !exists(pap, name)) {
            return new VoidResult();
        }

        return super.execute(ctx, pap);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Expression<String> getNameExpression() {
        return nameExpression;
    }

    public void setNameExpression(Expression<String> nameExpression) {
        this.nameExpression = nameExpression;
    }

    public boolean isIfExists() {
        return ifExists;
    }

    public void setIfExists(boolean ifExists) {
        this.ifExists = ifExists;
    }

    @Override
    public String toFormattedString(int indentLevel) {
        String typeStr = "";
        switch (type) {
            case PROHIBITION -> typeStr = "prohibition";
            case OBLIGATION -> typeStr = "obligation";
            case OPERATION -> typeStr = "operation";
            case NODE -> typeStr = "node";
        }
        typeStr = (ifExists ? "if exists " : "") + typeStr;

        return indent(indentLevel) + String.format("delete %s %s", typeStr, nameExpression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteStatement that)) {
            return false;
        }
        return ifExists == that.ifExists && type == that.type && Objects.equals(nameExpression, that.nameExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, nameExpression, ifExists);
    }

    /**
     * The kind of entity a {@link DeleteStatement} deletes.
     */
    public enum Type {
        NODE,
        PROHIBITION,
        OBLIGATION,
        OPERATION,
    }
}
