package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class ContainsKey extends PMLOperation {

    public ContainsKey() {
        super(
                "containsKey",
                Type.bool(),
                List.of("map", "key"),
                Map.of("map", Type.map(Type.any(), Type.any()), "key", Type.any())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Map<Value, Value> valueMap = ((Value) operands.get("map")).getMapValue();
        Value element = (Value) operands.get("key");
        boolean contains = valueMap.containsKey(element);
        return new BoolValue(contains);
    }
}
