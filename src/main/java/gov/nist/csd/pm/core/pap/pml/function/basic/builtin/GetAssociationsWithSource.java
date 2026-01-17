package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithSource extends PMLQueryFunction<List<Map<String, Object>>> {

    private static final Type<List<Map<String, Object>>> returnType = ListType.of(MapType.of(STRING_TYPE, ANY_TYPE));

    public GetAssociationsWithSource() {
        super(
                "getAssociationsWithSource",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public List<Map<String, Object>> execute(PolicyQuery query, Args args) throws PMException {
        String source = args.get(NODE_NAME_PARAM);

        long id = query.graph().getNodeId(source);
        Collection<Association> associations = query.graph().getAssociationsWithSource(id);
        List<Map<String, Object>> associationValues = new ArrayList<>();
        for (Association association : associations) {
            associationValues.add(Map.of(
                "ua", query.graph().getNodeById(association.getSource()).getName(),
                "target", query.graph().getNodeById(association.getTarget()).getName(),
                "arset", new ArrayList<>(association.getAccessRightSet())
            ));
        }

        return associationValues;
    }
}
