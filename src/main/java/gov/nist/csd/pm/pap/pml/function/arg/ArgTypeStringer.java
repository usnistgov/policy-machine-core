package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.arg.type.BooleanType;
import gov.nist.csd.pm.pap.function.arg.type.ListType;
import gov.nist.csd.pm.pap.function.arg.type.LongType;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.function.arg.type.ObjectType;
import gov.nist.csd.pm.pap.function.arg.type.StringType;

public class ArgTypeStringer {

    public static String toPMLString(ArgType<?> argType) {
        return switch (argType) {
            case BooleanType booleanType -> "bool";
            case ListType<?> listType ->  "[]" + toPMLString(listType.getElementType());
            case LongType longType ->"long";
            case MapType<?, ?> mapType ->"map[" + toPMLString(mapType.getKeyType()) + "]" + toPMLString(mapType.getValueType());
            case ObjectType objectType -> "any";
            case StringType stringType -> "string";
            default -> throw new IllegalArgumentException("AccessRightSet is not a supported type in PML");
        };
    }
}
