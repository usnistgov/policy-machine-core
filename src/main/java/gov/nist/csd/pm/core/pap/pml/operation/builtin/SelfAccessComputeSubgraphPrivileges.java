package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.subgraph.SubgraphPrivileges;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfAccessComputeSubgraphPrivileges extends QueryOperation<Map<String, List<String>>> {

    private static final NodeNameFormalParameter NODE_NAME_PARAM =
        new NodeNameFormalParameter("node_name");

    public SelfAccessComputeSubgraphPrivileges() {
        super("self_compute_subgraph_privileges", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE)),
            List.of(NODE_NAME_PARAM), List.of());
    }

    @Override
    public Map<String, List<String>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        long nodeId = query.graph().getNodeId(nodeName);
        SubgraphPrivileges sp = query.access().self(userCtx).computeSubgraphPrivileges(nodeId);
        return flatten(sp);
    }

    private Map<String, List<String>> flatten(SubgraphPrivileges sp) {
        Map<String, List<String>> result = new HashMap<>();
        result.put(sp.node().getName(), new ArrayList<>(sp.privileges()));
        for (SubgraphPrivileges child : sp.ascendants()) {
            result.putAll(flatten(child));
        }
        return result;
    }
}
