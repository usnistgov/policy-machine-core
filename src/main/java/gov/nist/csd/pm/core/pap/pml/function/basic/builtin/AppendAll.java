package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.builtin.Append.DST_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.List;

public class AppendAll extends PMLBasicFunction<List<Object>> {

    public static final FormalParameter<List<Object>> SRC_LIST_PARAM = new FormalParameter<>("src", ListType.of(ANY_TYPE));

    public AppendAll() {
        super(
                "appendAll",
                ListType.of(ANY_TYPE),
                List.of(DST_PARAM, SRC_LIST_PARAM)
        );
    }

    @Override
    protected List<Object> execute(Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        List<Object> srcValue = args.get(SRC_LIST_PARAM);

        valueArr.addAll(srcValue);

        return valueArr;
    }
}
