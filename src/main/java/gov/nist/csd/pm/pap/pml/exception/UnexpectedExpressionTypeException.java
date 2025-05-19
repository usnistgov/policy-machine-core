package gov.nist.csd.pm.pap.pml.exception;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.type.TypeStringer;

public class UnexpectedExpressionTypeException extends PMException {

    public UnexpectedExpressionTypeException(Type<?> objectType, Type<?> castingType) {
        super(String.format("expected expression type " +
            TypeStringer.toPMLString(castingType) +
            ", got " +
            TypeStringer.toPMLString(objectType)));
    }
}
