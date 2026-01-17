package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAdjacentAscendants extends PMLQueryFunction<List<String>> {

    private static final Type<List<String>> returnType = ListType.of(STRING_TYPE);

    public GetAdjacentAscendants() {
        super(
                "getAdjacentAscendants",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public List<String> execute(PolicyQuery query, Args args) throws PMException {
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
