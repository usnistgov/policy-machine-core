package gov.nist.csd.pm.core.pap.pml.exception;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.type.TypeStringer;

public class UnexpectedExpressionTypeException extends PMException {

    public UnexpectedExpressionTypeException(Type<?> objectType, Type<?> castingType) {
        super(String.format("expected expression type " +
            TypeStringer.toPMLString(castingType) +
            ", got " +
            TypeStringer.toPMLString(objectType)));
    }
}
