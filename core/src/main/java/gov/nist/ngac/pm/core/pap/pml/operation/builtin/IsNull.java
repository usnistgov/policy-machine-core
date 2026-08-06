package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

/**
 * A PML built-in function that returns whether a value is null.
 */
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
