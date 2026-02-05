package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;
import java.util.Map;


public class GetNode extends PMLQueryOperation<Map<String, Object>> {

    public GetNode() {
        super(
                "getNode",
                MapType.of(STRING_TYPE, ANY_TYPE),
                List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public Map<String, Object> execute(PolicyQuery query, Args args) throws PMException {
        Node node = query.graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return Map.of(
            "name", node.getName(),
            "type", node.getType().toString(),
            "properties", node.getProperties()
        );
    }
}
