package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;
import java.util.Map;

public class ContainsKey extends PMLBasicFunction {

    public static final FormalParameter<Map<Object, Object>> MAP_PARAM = new FormalParameter<>("map", MapType.of(
        ANY_TYPE, ANY_TYPE));
    public static final FormalParameter<Object> KEY_PARAM = new FormalParameter<>("key", ANY_TYPE);


    public ContainsKey() {
        super(
                "containsKey",
                BOOLEAN_TYPE,
                List.of(MAP_PARAM, KEY_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        Map<Object, Object> valueMap = args.get(MAP_PARAM);
        Object element = args.get(KEY_PARAM);
        return valueMap.containsKey(element);
    }
}
