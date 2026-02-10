package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

public class Append extends PMLFunctionOperation<List<Object>> {

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
    public List<Object> execute(Args args) throws PMException {
        List<Object> valueArr = args.get(DST_PARAM);
        Object srcValue = args.get(SRC_PARAM);

        valueArr.add(srcValue);

        return valueArr;
    }
}
