package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.common.graph.node.NodeType;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.*;

public class Search extends PMLBasicFunction {

    public static final FormalParameter<String> TYPE_ARG = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_ARG = new FormalParameter<>("properties", mapType(STRING_TYPE, STRING_TYPE));


    public Search() {
        super(
                "search",
                listType(STRING_TYPE),
                List.of(TYPE_ARG, PROPERTIES_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        NodeType nodeType = NodeType.toNodeType(args.get(TYPE_ARG));

        Map<String, String> properties = args.get(PROPERTIES_ARG);
        Collection<Node> search = pap.query().graph().search(nodeType, properties);

        List<Object> ret = new ArrayList<>(search.size());
        for (Node n : search) {
            ret.add(objectMapper.convertValue(n, Map.class));
        }

        return ret;
    }
}
