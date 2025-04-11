package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.OBJECT_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.List;

public class Contains extends PMLBasicFunction {

    public static final FormalParameter<List<Object>> ARR_ARG = new FormalParameter<>("arr", listType(OBJECT_TYPE));
    public static final FormalParameter<Object> ELEMENT_ARG = new FormalParameter<>("element", OBJECT_TYPE);

    public Contains() {
        super(
                "contains",
                BOOLEAN_TYPE,
                List.of(ARR_ARG, ELEMENT_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        List<Object> valueArr = args.get(ARR_ARG);
        Object element = args.get(ELEMENT_ARG);
        return valueArr.contains(element);
    }
}

