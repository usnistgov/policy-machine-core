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
