package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;

public class Append extends PMLBasicFunction {

    public static final PMLFormalArg DST_ARG = new PMLFormalArg("dst", Type.array(Type.any()));
    public static final PMLFormalArg SRC_ARG = new PMLFormalArg("src", Type.any());

    public Append() {
        super(
                "append",
                Type.array(Type.any()),
                List.of(DST_ARG, SRC_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        List<Value> valueArr = args.get(DST_ARG).getArrayValue();
        Value srcValue = args.get(SRC_ARG);

        valueArr.add(srcValue);

        return new ArrayValue(valueArr, Type.array(Type.any()));
    }
}
