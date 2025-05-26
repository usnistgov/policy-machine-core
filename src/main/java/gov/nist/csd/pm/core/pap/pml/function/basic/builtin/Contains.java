package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Contains extends PMLBasicFunction {

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
    public Object execute(PAP pap, Args args) throws PMException {
        List<Object> valueArr = args.get(ARR_PARAM);
        Object element = args.get(ELEMENT_PARAM);
        return valueArr.contains(element);
    }
}

