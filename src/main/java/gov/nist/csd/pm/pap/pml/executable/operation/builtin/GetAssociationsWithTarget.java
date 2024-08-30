package gov.nist.csd.pm.pap.pml.executable.operation.builtin;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithTarget extends PMLOperation {

    private static final Type returnType = Type.array(Type.map(Type.string(), Type.any()));

    public GetAssociationsWithTarget() {
        super(
                "getAssociationsWithTarget",
                returnType,
                List.of("target"),
                Map.of("target", Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Value source = (Value) operands.get("target");
        Collection<Association> associations = pap.query().graph().getAssociationsWithTarget(source.getStringValue());
        List<Value> associationValues = new ArrayList<>(associations.size());
        for (Association association : associations) {
            associationValues.add(Value.fromObject(association));
        }

        return new ArrayValue(associationValues, returnType);
    }
}
