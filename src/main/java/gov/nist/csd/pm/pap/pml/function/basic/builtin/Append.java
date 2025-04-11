package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Append extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> DST_ARG = new FormalParameter<>("dst", listType(OBJECT_TYPE));
    public static final FormalParameter<Object> SRC_ARG = new FormalParameter<>("src", OBJECT_TYPE);

    public Append() {
        super(
                "append",
                listType(OBJECT_TYPE),
                List.of(DST_ARG, SRC_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        List<Object> valueArr = args.get(DST_ARG);
        Object srcValue = args.get(SRC_ARG);

        valueArr.add(srcValue);

        return valueArr;
    }
}
