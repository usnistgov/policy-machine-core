package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAdjacentAscendants extends PMLOperation {

    private static final Type returnType = Type.array(Type.string());

    public GetAdjacentAscendants() {
        super(
                "getAdjacentAscendants",
                returnType,
                List.of("nodeName"),
                Map.of("nodeName", Type.string())
        );
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Value nodeName = (Value) operands.get("nodeName");

        Collection<String> ascendants = pap.query().graph().getAdjacentAscendants(nodeName.getStringValue());
        List<Value> ascValues = new ArrayList<>(ascendants.size());

        ascendants.forEach(ascendant -> ascValues.add(new StringValue(ascendant)));

        return new ArrayValue(ascValues, returnType);
    }
}