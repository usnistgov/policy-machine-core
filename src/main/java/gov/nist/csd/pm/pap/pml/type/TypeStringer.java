package gov.nist.csd.pm.pap.pml.type;

import gov.nist.csd.pm.pap.function.arg.type.AnyType;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.arg.type.BooleanType;
import gov.nist.csd.pm.pap.function.arg.type.ListType;
import gov.nist.csd.pm.pap.function.arg.type.LongType;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.function.arg.type.StringType;
import gov.nist.csd.pm.pap.function.arg.type.VoidType;

public class TypeStringer {

    public static String toPMLString(Type<?> type) {
        return switch (type) {
            case BooleanType booleanType -> "bool";
            case ListType<?> listType ->  "[]" + toPMLString(listType.getElementType());
            case LongType longType ->"long";
            case MapType<?, ?> mapType ->"map[" + toPMLString(mapType.getKeyType()) + "]" + toPMLString(mapType.getValueType());
            case AnyType anyType -> "any";
            case StringType stringType -> "string";
            case VoidType voidType -> "void";
            default -> throw new IllegalArgumentException(type + "is not a supported type in PML");
        };
    }
}
