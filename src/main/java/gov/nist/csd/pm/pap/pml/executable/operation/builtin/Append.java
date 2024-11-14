package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.List;
import java.util.Map;

public class Append extends PMLOperation {
    public Append() {
        super(
                "append",
                Type.array(Type.any()),
                List.of("dst", "src"),
                Map.of("dst", Type.array(Type.any()), "src", Type.any())
        );
    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        List<Value> valueArr = (List<Value>) operands.get("dst");
        Value srcValue = (Value) operands.get("src");

        valueArr.add(srcValue);

        return new ArrayValue(valueArr, Type.array(Type.any()));
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }
}
