package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentDescendants extends PMLBasicFunction {

    private static final Type<?> returnType = ListType.of(STRING_TYPE);

    public GetAdjacentDescendants() {
        super(
                "getAdjacentDescendants",
                ListType.of(STRING_TYPE),
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
