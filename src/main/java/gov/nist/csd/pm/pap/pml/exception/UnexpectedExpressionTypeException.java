package gov.nist.csd.pm.pap.pml.exception;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.function.arg.ArgTypeStringer;

public class UnexpectedExpressionTypeException extends PMException {

    public UnexpectedExpressionTypeException(Type<?> objectType, Type<?> castingType) {
        super(String.format("expected expression type " +
            ArgTypeStringer.toPMLString(castingType) +
            ", got " +
            ArgTypeStringer.toPMLString(objectType)));
    }
}
