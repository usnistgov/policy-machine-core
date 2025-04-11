package gov.nist.csd.pm.pap.pml.expression;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    public <S> Expression<S> asType(ArgType<S> targetType) {
        if (!getType().isCastableTo(targetType)) {
            throw new IllegalArgumentException("Cannot cast from " + getType() + " to " + targetType);
        }

        return (Expression<S>) this;
    }
}