package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

/**
 * PML built-in function append(dst, src): appends src to the dst list in place and returns dst.
 */
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
