package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentDescendants extends PMLQueryOperation<List<String>> {

    public GetAdjacentDescendants() {
        super(
            "getAdjacentDescendants",
            ListType.of(STRING_TYPE),
            List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public List<String> execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);

        long id = query.graph().getNodeId(nodeName);
        Collection<Long> descendants = query.graph().getAdjacentDescendants(id);
        List<String> descValues = new ArrayList<>();
        for (long desc : descendants) {
            Node node = query.graph().getNodeById(desc);
            descValues.add(node.getName());
        }

        return descValues;
    }
}
