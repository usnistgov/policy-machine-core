package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfAccessComputePersonalObjectSystem extends QueryOperation<Map<String, List<String>>> {

    public SelfAccessComputePersonalObjectSystem() {
        super("self_compute_personal_object_system", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE)),
            List.of(), List.of());
    }

    @Override
    public Map<String, List<String>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        Map<Node, AccessRightSet> posMap = query.access().self(userCtx).computePersonalObjectSystem();
        Map<String, List<String>> ret = new HashMap<>();
        for (Map.Entry<Node, AccessRightSet> e : posMap.entrySet()) {
            ret.put(e.getKey().getName(), new ArrayList<>(e.getValue()));
        }
        return ret;
    }
}
