package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAdjacentDescendants extends PMLOperation {

    private static final Type returnType = Type.array(Type.string());

    public GetAdjacentDescendants() {
        super(
                "getAdjacentDescendants",
                Type.array(Type.any()),
                List.of("nodeName"),
                Map.of("nodeName", Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Value nodeName = (Value) operands.get("nodeName");

        Collection<String> ascendants = pap.query().graph().getAdjacentDescendants(nodeName.getStringValue());
        List<Value> ascValues = new ArrayList<>(ascendants.size());

        ascendants.forEach(ascendant -> ascValues.add(new StringValue(ascendant)));

        return new ArrayValue(ascValues, returnType);
    }
}
