package gov.nist.csd.pm.pap.pml.function.builtin;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentAscendants extends PMLFunction {

    private static final Type returnType = Type.array(Type.string());

    public GetAdjacentAscendants() {
        super(
                "getAdjacentAscendants",
                returnType,
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Value nodeName = actualArgs.get(NODE_NAME_ARG);

        long id = pap.query().graph().getNodeId(nodeName.getStringValue());
        Collection<Long> ascendants = pap.query().graph().getAdjacentAscendants(id);
        List<Value> ascValues = new ArrayList<>(ascendants.size());
        for (long asc : ascendants) {
            Node node = pap.query().graph().getNodeById(asc);
            ascValues.add(new StringValue(node.getName()));
        }

        return new ArrayValue(ascValues, returnType);
    }
}
