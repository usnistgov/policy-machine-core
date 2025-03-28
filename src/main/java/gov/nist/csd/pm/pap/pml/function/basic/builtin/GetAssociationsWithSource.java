package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAssociationsWithSource extends PMLBasicFunction {

    private static final Type returnType = Type.array(Type.map(Type.string(), Type.any()));

    public GetAssociationsWithSource() {
        super(
                "getAssociationsWithSource",
                returnType,
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Value source = actualArgs.get(NODE_NAME_ARG);

        long id = pap.query().graph().getNodeId(source.getStringValue());
        Collection<Association> associations = pap.query().graph().getAssociationsWithSource(id);
        List<Value> associationValues = new ArrayList<>(associations.size());
        for (Association association : associations) {
            associationValues.add(Value.fromObject(association));
        }

        return new ArrayValue(associationValues, returnType);
    }
}
