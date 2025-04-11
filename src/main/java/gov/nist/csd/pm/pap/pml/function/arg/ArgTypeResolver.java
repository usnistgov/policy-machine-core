package gov.nist.csd.pm.pap.pml.function.arg;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.AnyTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.ArrayVarTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BooleanTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.MapVarTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.StringTypeContext;

public class ArgTypeResolver {

    public static ArgType<?> resolveFromParserCtx(PMLParser.VariableTypeContext ctx) {
        return switch (ctx) {
            case AnyTypeContext anyTypeContext -> OBJECT_TYPE;
            case StringTypeContext stringTypeContext -> STRING_TYPE;
            case BooleanTypeContext booleanTypeContext -> BOOLEAN_TYPE;
            case ArrayVarTypeContext arrayVarTypeContext ->
                listType(resolveFromParserCtx(arrayVarTypeContext.arrayType().variableType()));
            case MapVarTypeContext mapVarTypeContext ->
                mapType(
                    resolveFromParserCtx(mapVarTypeContext.mapType().keyType),
                    resolveFromParserCtx(mapVarTypeContext.mapType().valueType)
                );
            default -> OBJECT_TYPE;
        };
    }

}
