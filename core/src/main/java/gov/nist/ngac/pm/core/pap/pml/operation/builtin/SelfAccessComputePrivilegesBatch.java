package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.List;

/**
 * A PML built-in query that returns the caller's privileges on a list of nodes.
 */
public class SelfAccessComputePrivilegesBatch extends QueryOperation<List<List<String>>> {

    private static final FormalParameter<List<String>> NODE_NAMES_PARAM =
        new FormalParameter<>("node_names", ListType.of(STRING_TYPE));

    public SelfAccessComputePrivilegesBatch() {
        super("self_compute_privileges_batch", ListType.of(ListType.of(STRING_TYPE)),
            List.of(NODE_NAMES_PARAM), List.of());
    }

    @Override
    public List<List<String>> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        List<String> nodeNames = args.get(NODE_NAMES_PARAM);
        List<TargetContext> targets = new ArrayList<>(nodeNames.size());
        for (String name : nodeNames) {
            targets.add(NodeTargetContext.of(query.graph().getNodeId(name)));
        }
        List<AccessRightSet> results = query.access().self(userCtx).computePrivileges(targets);
        List<List<String>> ret = new ArrayList<>(results.size());
        for (AccessRightSet ars : results) {
            ret.add(new ArrayList<>(ars));
        }
        return ret;
    }
}
