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

public class GetAdjacentDescendants extends PMLBasicFunction {

    private static final Type<?> returnType = listType(STRING_TYPE);

    public GetAdjacentDescendants() {
        super(
                "getAdjacentDescendants",
                listType(STRING_TYPE),
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);

        long id = pap.query().graph().getNodeId(nodeName);
        Collection<Long> descendants = pap.query().graph().getAdjacentDescendants(id);
        List<String> descValues = new ArrayList<>();
        for (long desc : descendants) {
            Node node = pap.query().graph().getNodeById(desc);
            descValues.add(node.getName());
        }

        return descValues;
    }
}
