package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.exception.UnexpectedExpressionTypeException;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;

/**
 * Represents a compiled PML expression that evaluates to a value of type T.
 *
 * @param <T> The Java type this expression evaluates to.
 */
public abstract class Expression<T> extends PMLStatement<T> {

    /**
     * Gets the PML type of the value this expression evaluates to.
     *
     * @return The ArgType representing the expression's result type.
     */
    public abstract ArgType<T> getType();

    public <S> Expression<S> asType(ArgType<S> targetType) throws UnexpectedExpressionTypeException {
        if (!getType().isCastableTo(targetType)) {
            throw new UnexpectedExpressionTypeException(getType(), targetType);
        }

        return (Expression<S>) this;
    }
}