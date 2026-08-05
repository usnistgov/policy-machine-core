package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * PML built-in query get_adjacent_ascendants(node): returns the names of the nodes directly ascendant
 * to the given node.
 */
public class GetAdjacentAscendants extends PMLQueryOperation<List<String>> {

    private static final Type<List<String>> returnType = ListType.of(STRING_TYPE);

    public GetAdjacentAscendants() {
        super(
                "get_adjacent_ascendants",
                returnType,
                List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public List<String> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);

        long id = query.graph().getNodeId(nodeName);
        Collection<Long> ascendants = query.graph().getAdjacentAscendants(id);
        List<String> ascValues = new ArrayList<>();
        for (long asc : ascendants) {
            Node node = query.graph().getNodeById(asc);
            ascValues.add(node.getName());
        }

        return ascValues;
    }
}
