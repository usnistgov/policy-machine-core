package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentAscendants extends PMLBasicFunction {

    private static final Type<?> returnType = listType(STRING_TYPE);

    public GetAdjacentAscendants() {
        super(
                "getAdjacentAscendants",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);

        long id = pap.query().graph().getNodeId(nodeName);
        Collection<Long> ascendants = pap.query().graph().getAdjacentAscendants(id);
        List<String> ascValues = new ArrayList<>();
        for (long asc : ascendants) {
            Node node = pap.query().graph().getNodeById(asc);
            ascValues.add(node.getName());
        }

        return ascValues;
    }
}
