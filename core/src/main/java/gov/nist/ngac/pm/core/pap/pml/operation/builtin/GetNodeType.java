package gov.nist.ngac.pm.core.pap.pml.operation.builtin;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * PML built-in query get_node_type(node). Returns the node's type as a string.
 */
public class GetNodeType extends PMLQueryOperation<String> {

    public GetNodeType() {
        super(
                "get_node_type",
                STRING_TYPE,
                List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public String execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        Node node = query.graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return node.getType().toString();
    }
}

