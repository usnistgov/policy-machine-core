package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Append.DST_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class AppendAll extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> SRC_LIST_ARG = new FormalParameter<>("src", listType(OBJECT_TYPE));

    public AppendAll() {
        super(
                "appendAll",
                listType(OBJECT_TYPE),
                List.of(DST_ARG, SRC_LIST_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        List<Object> valueArr = args.get(DST_ARG);
        List<Object> srcValue = args.get(SRC_LIST_ARG);

        valueArr.addAll(srcValue);

        return valueArr;
    }
}
