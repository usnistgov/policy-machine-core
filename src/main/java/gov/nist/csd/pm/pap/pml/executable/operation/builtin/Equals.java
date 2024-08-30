package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;


public class Equals extends PMLOperation {

    private static final String VALUE1 = "value1";
    private static final String VALUE2 = "value2";

    public Equals() {
        super(
                "equals",
                Type.bool(),
                List.of(VALUE1, VALUE2),
                Map.of(VALUE1, Type.any(), VALUE2, Type.any())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Value v1 = (Value) operands.get(VALUE1);
        Value v2 = (Value) operands.get(VALUE2);

        return new BoolValue(v1.equals(v2));
    }
}
