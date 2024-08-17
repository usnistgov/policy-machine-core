package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

public class Contains extends PMLOperation {

    public Contains() {
        super(
                "contains",
                Type.bool(),
                List.of("arr", "element"),
                Map.of("arr", Type.array(Type.any()), "element", Type.any())
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        List<Value> valueArr = ((Value) operands.get("arr")).getArrayValue();
        Value element = (Value) operands.get("element");
        boolean contains = valueArr.contains(element);
        return new BoolValue(contains);
    }
}

