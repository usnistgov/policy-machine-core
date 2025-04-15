package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Contains extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> ARR_PARAM = new FormalParameter<>("arr", listType(ANY_TYPE));
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

