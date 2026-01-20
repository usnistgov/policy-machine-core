package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLBasicOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public class GetNodeType extends PMLQueryOperation<String> {

    public GetNodeType() {
        super(
                "getNodeType",
                STRING_TYPE,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public String execute(PolicyQuery query, Args args) throws PMException {
        Node node = query.graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return node.getType().toString();
    }
}

