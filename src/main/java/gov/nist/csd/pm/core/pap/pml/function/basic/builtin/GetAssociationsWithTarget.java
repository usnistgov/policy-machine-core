package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.Association;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithTarget extends PMLBasicFunction {

    private static final Type<?> returnType = ListType.of(MapType.of(STRING_TYPE, ANY_TYPE));

    public GetAssociationsWithTarget() {
        super(
            "getAssociationsWithTarget",
            returnType,
            List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public List<Map<String, Object>> execute(PAP pap, Args args) throws PMException {
        String target = args.get(NODE_NAME_PARAM);

        long id = pap.query().graph().getNodeId(target);
        Collection<Association> associations = pap.query().graph().getAssociationsWithTarget(id);
        List<Map<String, Object>> associationValues = new ArrayList<>();
        for (Association association : associations) {
            associationValues.add(Map.of(
                "ua", pap.query().graph().getNodeById(association.getSource()).getName(),
                "target", pap.query().graph().getNodeById(association.getTarget()).getName(),
                "arset", new ArrayList<>(association.getAccessRightSet())
            ));
        }

        return associationValues;
    }
}
