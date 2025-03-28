package gov.nist.csd.pm.pap.pml.function.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;

public class Append extends PMLFunction {

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
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        List<Value> valueArr = actualArgs.get(DST_ARG).getArrayValue();
        Value srcValue = actualArgs.get(SRC_ARG);

        valueArr.add(srcValue);

        return new ArrayValue(valueArr, Type.array(Type.any()));
    }
}
