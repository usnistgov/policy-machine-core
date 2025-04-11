package gov.nist.csd.pm.pap.pml.exception;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.function.arg.ArgTypeStringer;
import org.antlr.v4.runtime.ParserRuleContext;

public class ArgTypeNotCastableException extends PMLCompilationRuntimeException {

    public ArgTypeNotCastableException(ParserRuleContext ctx, ArgType<?> objectType, ArgType<?> castingType) {
        super(ctx, String.format("Cannot cast from ArgType " +
            ArgTypeStringer.toPMLString(objectType) +
            " to ArgType " +
            ArgTypeStringer.toPMLString(castingType)));
    }
}
