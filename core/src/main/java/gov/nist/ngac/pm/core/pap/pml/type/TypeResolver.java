package gov.nist.ngac.pm.core.pap.pml.type;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.ArrayVarTypeContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.BooleanTypeContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.Int64TypeContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.MapVarTypeContext;
import gov.nist.ngac.pm.core.pap.pml.antlr.PMLParser.StringTypeContext;

/**
 * Resolves a parsed PML type annotation into its core {@link Type}.
 */
public class TypeResolver {

    /**
     * Converts a parsed variable type context into its core {@link Type}, recursing into list and map
     * element types. An unrecognized context resolves to ANY_TYPE.
     */
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
