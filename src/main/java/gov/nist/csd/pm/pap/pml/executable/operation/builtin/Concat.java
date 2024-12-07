package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class Concat extends PMLOperation {

    private static final String ARR_ARG = "arr";

    public Concat() {
        super(
                "concat",
                Type.string(),
                List.of(ARR_ARG),
                Map.of("concat", Type.array(Type.string()))
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        List<Value> arr = ((Value) operands.get(ARR_ARG)).getArrayValue();
        StringBuilder s = new StringBuilder();
        for (Value v : arr) {
            s.append(v.getStringValue());
        }

        return new StringValue(s.toString());
    }
}
