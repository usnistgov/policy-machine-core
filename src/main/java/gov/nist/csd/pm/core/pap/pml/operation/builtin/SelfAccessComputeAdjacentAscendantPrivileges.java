package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfAccessComputeAdjacentAscendantPrivileges extends QueryOperation<Map<String, List<String>>> {

    private static final NodeNameFormalParameter NODE_NAME_PARAM =
        new NodeNameFormalParameter("node_name");

    public SelfAccessComputeAdjacentAscendantPrivileges() {
        super("selfAccessComputeAdjacentAscendantPrivileges", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE)),
            List.of(NODE_NAME_PARAM), List.of());
    }

    @Override
    public Map<String, List<String>> execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        long nodeId = query.graph().getNodeId(nodeName);
        Map<Node, AccessRightSet> arsetMap = query.selfAccess().computeAdjacentAscendantPrivileges(nodeId);
        Map<String, List<String>> ret = new HashMap<>();
        for (Map.Entry<Node, AccessRightSet> e : arsetMap.entrySet()) {
            ret.put(e.getKey().getName(), new ArrayList<>(e.getValue()));
        }
        return ret;
    }
}
