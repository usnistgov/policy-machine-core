package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import java.util.ArrayList;
import java.util.List;

public class SelfAccessComputePrivileges extends QueryOperation<List<String>> {

    private static final NodeNameFormalParameter NODE_NAME_PARAM =
        new NodeNameFormalParameter("node_name");

    public SelfAccessComputePrivileges() {
        super("selfAccessComputePrivileges", ListType.of(STRING_TYPE), List.of(NODE_NAME_PARAM), List.of());
    }

    @Override
    public List<String> execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        long nodeId = query.graph().getNodeId(nodeName);
        return new ArrayList<>(query.selfAccess().computePrivileges(new TargetContext(nodeId)));
    }
}
