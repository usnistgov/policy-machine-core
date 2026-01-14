package gov.nist.csd.pm.core.pap.pml.type;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.ArrayVarTypeContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.BooleanTypeContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.Int64TypeContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.MapVarTypeContext;
import gov.nist.csd.pm.core.pap.pml.antlr.PMLParser.StringTypeContext;

public class TypeResolver {

    public static Type<?> resolveFromParserCtx(PMLParser.VariableTypeContext ctx) {
        return switch (ctx) {
            case StringTypeContext stringTypeContext -> STRING_TYPE;
            case BooleanTypeContext booleanTypeContext -> BOOLEAN_TYPE;
            case Int64TypeContext int64TypeContext -> LONG_TYPE;
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
