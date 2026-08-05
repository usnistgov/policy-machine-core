package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PML built-in query self_compute_adjacent_descendant_privileges(node_name): returns the caller's
 * privileges on each node directly descendant from the given node, keyed by node name.
 */
public class SelfAccessComputeAdjacentDescendantPrivileges extends QueryOperation<Map<String, List<String>>> {

    private static final NodeNameFormalParameter NODE_NAME_PARAM =
        new NodeNameFormalParameter("node_name");

    public SelfAccessComputeAdjacentDescendantPrivileges() {
        super("self_compute_adjacent_descendant_privileges", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE)),
            List.of(NODE_NAME_PARAM), List.of());
    }

    @Override
    public Map<String, List<String>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        long nodeId = query.graph().getNodeId(nodeName);
        Map<Node, AccessRightSet> arsetMap = query.access().self(userCtx).computeAdjacentDescendantPrivileges(nodeId);
        Map<String, List<String>> ret = new HashMap<>();
        for (Map.Entry<Node, AccessRightSet> e : arsetMap.entrySet()) {
            ret.put(e.getKey().getName(), new ArrayList<>(e.getValue()));
        }
        return ret;
    }
}
