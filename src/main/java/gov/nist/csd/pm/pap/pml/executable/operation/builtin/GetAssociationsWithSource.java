package gov.nist.csd.pm.pap.pml.executable.operation.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperation;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithSource extends PMLOperation {

    private static final Type returnType = Type.array(Type.map(Type.string(), Type.any()));

    public GetAssociationsWithSource() {
        super(
                "getAssociationsWithSource",
                returnType,
                List.of("source"),
                Map.of("source", Type.string())
        );
    }

    @Override
    public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Map<String, Object> operands) throws PMException {

    }

    @Override
    public Value execute(PAP pap, Map<String, Object> operands) throws PMException {
        Value source = (Value) operands.get("source");
        Collection<Association> associations = pap.query().graph().getAssociationsWithSource(source.getStringValue());
        List<Value> associationValues = new ArrayList<>(associations.size());
        for (Association association : associations) {
            associationValues.add(Value.fromObject(association));
        }

        return new ArrayValue(associationValues, returnType);
    }
}
