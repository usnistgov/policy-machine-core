package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentDescendants extends PMLBasicFunction {

    private static final Type returnType = Type.array(Type.string());

    public GetAdjacentDescendants() {
        super(
                "getAdjacentDescendants",
                Type.array(Type.any()),
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Value nodeName = actualArgs.get(NODE_NAME_ARG);

        long id = pap.query().graph().getNodeId(nodeName.getStringValue());
        Collection<Long> descendants = pap.query().graph().getAdjacentDescendants(id);
        List<Value> descValues = new ArrayList<>(descendants.size());

        for (long desc : descendants) {
            Node node = pap.query().graph().getNodeById(desc);
            descValues.add(new StringValue(node.getName()));
        }

        return new ArrayValue(descValues, returnType);
    }
}
