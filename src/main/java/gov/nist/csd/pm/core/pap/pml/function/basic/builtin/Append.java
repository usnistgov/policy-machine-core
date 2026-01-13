package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Append extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> DST_PARAM = new FormalParameter<>("dst", ListType.of(ANY_TYPE));
    public static final FormalParameter<Object> SRC_PARAM = new FormalParameter<>("src", ANY_TYPE);

    public Append() {
        super(
                "append",
                ListType.of(ANY_TYPE),
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
