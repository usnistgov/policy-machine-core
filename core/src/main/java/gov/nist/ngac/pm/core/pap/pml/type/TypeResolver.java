/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

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
