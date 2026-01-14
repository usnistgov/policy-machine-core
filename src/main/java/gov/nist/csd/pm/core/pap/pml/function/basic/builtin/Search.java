package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Search extends PMLBasicFunction {

    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));


    public Search() {
        super(
                "search",
                ListType.of(STRING_TYPE),
                List.of(TYPE_PARAM, PROPERTIES_PARAM)
        );
    }

    @Override
    public List<Map<String, Object>> execute(PAP pap, Args args) throws PMException {
        NodeType nodeType = NodeType.toNodeType(args.get(TYPE_PARAM));

        Map<String, String> properties = args.get(PROPERTIES_PARAM);
        Collection<Node> search = pap.query().graph().search(nodeType, properties);

        List<Map<String, Object>> ret = new ArrayList<>(search.size());
        for (Node node : search) {
            ret.add(Map.of(
                "name", node.getName(),
                "type", node.getType().toString(),
                "properties", node.getProperties()
            ));
        }

        return ret;
    }
}
