package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Append.DST_PARAM;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class AppendAll extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> SRC_LIST_PARAM = new FormalParameter<>("src", listType(ANY_TYPE));

    public AppendAll() {
        super(
                "appendAll",
                listType(ANY_TYPE),
                List.of(DST_PARAM, SRC_LIST_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        List<Object> srcValue = args.get(SRC_LIST_PARAM);

        valueArr.addAll(srcValue);

        return valueArr;
    }
}
