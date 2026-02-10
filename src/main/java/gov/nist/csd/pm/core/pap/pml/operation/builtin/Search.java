package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.common.graph.node.NodeType;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Search extends PMLQueryOperation<List<Map<String, Object>>> {

    public static final FormalParameter<String> TYPE_PARAM = new FormalParameter<>("type", STRING_TYPE);
    public static final FormalParameter<Map<String, String>> PROPERTIES_PARAM = new FormalParameter<>("properties", MapType.of(STRING_TYPE, STRING_TYPE));


    public Search() {
        super(
                "search",
                ListType.of(MapType.of(STRING_TYPE, ANY_TYPE)),
                List.of(TYPE_PARAM, PROPERTIES_PARAM),
            List.of()
        );
    }

    @Override
    public List<Map<String, Object>> execute(PolicyQuery query, Args args) throws PMException {
        NodeType nodeType = NodeType.toNodeType(args.get(TYPE_PARAM));

        Map<String, String> properties = args.get(PROPERTIES_PARAM);
        Collection<Node> search = query.graph().search(nodeType, properties);

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
