package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

public class IsNull extends PMLFunctionOperation<Boolean> {

    private static final FormalParameter<Object> OBJ_PARAM = new FormalParameter<>("obj", ANY_TYPE);

    public IsNull() {
        super("is_null", BOOLEAN_TYPE, List.of(OBJ_PARAM));
    }

    @Override
    public Boolean execute(Args args) throws PMException {
        Object o = args.get(OBJ_PARAM);
        return o == null;
    }
}
