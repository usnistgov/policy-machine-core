package gov.nist.csd.pm.pap.pml.executable.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;
import java.util.Map;

public class ContainsKey extends PMLFunction {

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
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Map<Value, Value> valueMap = actualArgs.get(MAP_ARG).getMapValue();
        Value element = actualArgs.get(KEY_ARG);
        boolean contains = valueMap.containsKey(element);
        return new BoolValue(contains);
    }
}
