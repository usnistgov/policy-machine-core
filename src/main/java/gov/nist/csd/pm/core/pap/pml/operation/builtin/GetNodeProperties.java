package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLBasicOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;
import java.util.Map;

public class GetNodeProperties extends QueryOperation<Map<String, String>> {

    private static final Type<Map<String, String>> returnType = MapType.of(STRING_TYPE, STRING_TYPE);


    public GetNodeProperties() {
        super(
                "getNodeProperties",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Map<String, String> execute(PolicyQuery query, Args args) throws PMException {
        Node node = query.graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return node.getProperties();
    }
}
