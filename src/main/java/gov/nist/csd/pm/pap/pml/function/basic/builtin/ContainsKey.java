package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;
import java.util.Map;

public class ContainsKey extends PMLBasicFunction {

    public static final PMLFormalArg MAP_ARG = new PMLFormalArg("map", Type.map(Type.any(), Type.any()));
    public static final PMLFormalArg KEY_ARG = new PMLFormalArg("key", Type.any());


    public ContainsKey() {
        super(
                "containsKey",
                Type.bool(),
                List.of(MAP_ARG, KEY_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        Map<Value, Value> valueMap = args.get(MAP_ARG).getMapValue();
        Value element = args.get(KEY_ARG);
        boolean contains = valueMap.containsKey(element);
        return new BoolValue(contains);
    }
}
