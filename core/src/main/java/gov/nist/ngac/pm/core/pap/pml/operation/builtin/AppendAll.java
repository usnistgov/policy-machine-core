package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.builtin.Append.DST_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

public class AppendAll extends PMLFunctionOperation<List<Object>> {

    public static final FormalParameter<List<Object>> SRC_LIST_PARAM = new FormalParameter<>("src", ListType.of(ANY_TYPE));

    public AppendAll() {
        super(
                "append_all",
                ListType.of(ANY_TYPE),
                List.of(DST_PARAM, SRC_LIST_PARAM)
        );
    }

    @Override
    public List<Object> execute(Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        List<Object> srcValue = args.get(SRC_LIST_PARAM);

        valueArr.addAll(srcValue);

        return valueArr;
    }
}
