package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Append.DST_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;

public class AppendAll extends PMLBasicFunction {

    public static final PMLFormalArg SRC_LIST_ARG = new PMLFormalArg("src", Type.array(Type.any()));

    public AppendAll() {
        super(
                "appendAll",
                Type.array(Type.any()),
                List.of(DST_ARG, SRC_LIST_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        List<Value> valueArr = actualArgs.get(DST_ARG).getArrayValue();
        List<Value> srcValue = actualArgs.get(SRC_LIST_ARG).getArrayValue();

        valueArr.addAll(srcValue);

        return new ArrayValue(valueArr, Type.array(Type.any()));
    }
}
