package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.MapType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;
import java.util.Map;

public class GetNodeProperties extends QueryOperation<Map<String, String>> {

    private static final Type<Map<String, String>> returnType = MapType.of(STRING_TYPE, STRING_TYPE);


    public GetNodeProperties() {
        super(
                "get_node_properties",
                returnType,
                List.of(NODE_NAME_PARAM),
                List.of()
        );
    }

    @Override
    public Map<String, String> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        Node node = query.graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return node.getProperties();
    }
}
