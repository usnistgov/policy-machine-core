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

public class Contains extends PMLBasicFunction {

    public static final PMLFormalArg ARR_ARG = new PMLFormalArg("arr", Type.array(Type.any()));
    public static final PMLFormalArg ELEMENT_ARG = new PMLFormalArg("element", Type.any());

    public Contains() {
        super(
                "contains",
                Type.bool(),
                List.of(ARR_ARG, ELEMENT_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        List<Value> valueArr = args.get(ARR_ARG).getArrayValue();
        Value element = args.get(ELEMENT_ARG);
        boolean contains = valueArr.contains(element);
        return new BoolValue(contains);
    }
}

