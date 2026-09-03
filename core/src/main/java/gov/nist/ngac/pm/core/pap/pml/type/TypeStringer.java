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

import gov.nist.ngac.pm.core.pap.operation.arg.type.AnyType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.BooleanType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.LongType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.StringType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.arg.type.VoidType;

/**
 * Renders a core {@link Type} back to its PML source syntax.
 */
public class TypeStringer {

    /**
     * Converts a type to its PML source representation (e.g. "int64", "[]string", "map[string]any").
     *
     * @throws IllegalArgumentException if the type has no PML syntax (only reachable for a type outside
     * this sealed hierarchy)
     */
    public static String toPMLString(Type<?> type) {
        return switch (type) {
            case BooleanType booleanType -> "bool";
            case ListType<?> listType ->  "[]" + toPMLString(listType.getElementType());
            case LongType longType ->"int64";
            case MapType<?, ?> mapType ->"map[" + toPMLString(mapType.getKeyType()) + "]" + toPMLString(mapType.getValueType());
            case AnyType anyType -> "any";
            case StringType stringType -> "string";
            case VoidType voidType -> "void";
            default -> throw new IllegalArgumentException(type + "is not a supported type in PML");
        };
    }
}
