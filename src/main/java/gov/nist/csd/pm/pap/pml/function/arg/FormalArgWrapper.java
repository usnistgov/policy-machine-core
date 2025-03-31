package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.function.arg.type.BooleanType;
import gov.nist.csd.pm.pap.function.arg.type.ListType;
import gov.nist.csd.pm.pap.function.arg.type.LongType;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.function.arg.type.StringType;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.ArrayList;
import java.util.List;

public class FormalArgWrapper {

    public static List<WrappedFormalArg<?>> wrap(List<FormalArg<?>> formalArgs) {
        List<WrappedFormalArg<?>> wrappedFormalArgs = new ArrayList<>();
        for (FormalArg<?> formalArg : formalArgs) {
            wrappedFormalArgs.add(new WrappedFormalArg<>(formalArg, Type.any()));
        }

        return wrappedFormalArgs;
    }

    private static Type argTypeToPMLType(ArgType<?> type) {
        return switch (type) {
            case StringType stringType -> Type.string();
            case BooleanType booleanType -> Type.bool();
            case LongType longType -> Type.num();
            case ListType<?> listType -> Type.array(argTypeToPMLType(listType));
            case MapType<?, ?> mapType -> Type.map(argTypeToPMLType(mapType.getKeyType()), argTypeToPMLType(mapType.getValueType()));
            default -> throw new IllegalStateException("Unsupported type: " + type);
        };
    }
}
