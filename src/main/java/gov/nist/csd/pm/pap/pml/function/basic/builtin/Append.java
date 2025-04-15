package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Append extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> DST_PARAM = new FormalParameter<>("dst", listType(ANY_TYPE));
    public static final FormalParameter<Object> SRC_PARAM = new FormalParameter<>("src", ANY_TYPE);

    public Append() {
        super(
                "append",
                listType(ANY_TYPE),
                List.of(DST_PARAM, SRC_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        Object srcValue = args.get(SRC_PARAM);

        valueArr.add(srcValue);

        return valueArr;
    }
}
