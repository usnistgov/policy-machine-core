package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation;
import java.util.List;

public class Contains extends PMLFunctionOperation<Boolean> {

    public static final FormalParameter<List<Object>> ARR_PARAM = new FormalParameter<>("arr", ListType.of(ANY_TYPE));
    public static final FormalParameter<Object> ELEMENT_PARAM = new FormalParameter<>("element", ANY_TYPE);

    public Contains() {
        super(
                "contains",
                BOOLEAN_TYPE,
                List.of(ARR_PARAM, ELEMENT_PARAM)
        );
    }

    @Override
    public Boolean execute(Args args) throws PMException {
        List<Object> valueArr = args.get(ARR_PARAM);
        Object element = args.get(ELEMENT_PARAM);
        return valueArr.contains(element);
    }
}

