package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.graph.Association;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.operation.arg.type.MapType;
import gov.nist.csd.pm.core.pap.operation.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithTarget extends PMLQueryOperation<List<Map<String, Object>>> {

    private static final Type<List<Map<String, Object>>> returnType = ListType.of(MapType.of(STRING_TYPE, ANY_TYPE));

    public GetAssociationsWithTarget() {
        super(
            "getAssociationsWithTarget",
            returnType,
            List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public List<Map<String, Object>> execute(PolicyQuery query, Args args) throws PMException {
        String target = args.get(NODE_NAME_PARAM);

        long id = query.graph().getNodeId(target);
        Collection<Association> associations = query.graph().getAssociationsWithTarget(id);
        List<Map<String, Object>> associationValues = new ArrayList<>();
        for (Association association : associations) {
            associationValues.add(Map.of(
                "ua", query.graph().getNodeById(association.source()).getName(),
                "target", query.graph().getNodeById(association.target()).getName(),
                "arset", new ArrayList<>(association.arset())
            ));
        }

        return associationValues;
    }
}
