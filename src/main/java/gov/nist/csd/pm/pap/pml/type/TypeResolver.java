package gov.nist.csd.pm.pap.pml.type;

import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;


import gov.nist.csd.pm.pap.function.arg.type.ListType;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.AnyTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.ArrayVarTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.BooleanTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.MapVarTypeContext;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser.StringTypeContext;

public class TypeResolver {

    public static Type<?> resolveFromParserCtx(PMLParser.VariableTypeContext ctx) {
        return switch (ctx) {
            case AnyTypeContext anyTypeContext -> ANY_TYPE;
            case StringTypeContext stringTypeContext -> STRING_TYPE;
            case BooleanTypeContext booleanTypeContext -> BOOLEAN_TYPE;
            case ArrayVarTypeContext arrayVarTypeContext ->
                ListType.of(resolveFromParserCtx(arrayVarTypeContext.arrayType().variableType()));
            case MapVarTypeContext mapVarTypeContext ->
                MapType.of(
                    resolveFromParserCtx(mapVarTypeContext.mapType().keyType),
                    resolveFromParserCtx(mapVarTypeContext.mapType().valueType)
                );
            default -> ANY_TYPE;
        };
    }

}
