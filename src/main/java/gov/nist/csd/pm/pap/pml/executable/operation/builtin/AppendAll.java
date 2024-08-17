package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.List;
import java.util.Map;

public class AppendAll extends PMLOperation {
    public AppendAll() {
        super(
                "appendAll",
                Type.array(Type.any()),
                List.of("dst", "src"),
                Map.of("dst", Type.array(Type.any()), "src", Type.array(Type.any()))
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        List<Value> valueArr = (List<Value>) operands.get("dst");
        List<Value> srcValue = (List<Value>) operands.get("src");

        valueArr.addAll(srcValue);

        return new ArrayValue(valueArr, Type.array(Type.any()));
    }
}
