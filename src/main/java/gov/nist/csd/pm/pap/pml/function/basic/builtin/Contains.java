package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;

public class Contains extends PMLBasicFunction {

    public static final PMLFormalArg ARR_ARG = new PMLFormalArg("arr", Type.array(Type.any()));
    public static final PMLFormalArg ELEMENT_ARG = new PMLFormalArg("element", Type.any());

    public Contains() {
        super(
                "contains",
                Type.bool(),
                List.of(ARR_ARG, ELEMENT_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        List<Value> valueArr = actualArgs.get(ARR_ARG).getArrayValue();
        Value element = actualArgs.get(ELEMENT_ARG);
        boolean contains = valueArr.contains(element);
        return new BoolValue(contains);
    }
}

