package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;

import com.google.protobuf.BoolValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;



import java.util.List;
import java.util.Map;

public class ContainsKey extends PMLBasicFunction {

    public static final FormalParameter<Map<Object, Object>> MAP_ARG = new FormalParameter<>("map", ArgType.mapType(OBJECT_TYPE, OBJECT_TYPE));
    public static final FormalParameter<Object> KEY_ARG = new FormalParameter<>("key", OBJECT_TYPE);


    public ContainsKey() {
        super(
                "containsKey",
                BOOLEAN_TYPE,
                List.of(MAP_ARG, KEY_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        Map<Object, Object> valueMap = args.get(MAP_ARG);
        Object element = args.get(KEY_ARG);
        return valueMap.containsKey(element);
    }
}
