package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;

import com.fasterxml.jackson.core.type.TypeReference;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.relationship.Association;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetAssociationsWithSource extends PMLBasicFunction {

    private static final ArgType<?> returnType = ArgType.listType(ArgType.mapType(STRING_TYPE, ANY_TYPE));

    public GetAssociationsWithSource() {
        super(
                "getAssociationsWithSource",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String source = args.get(NODE_NAME_PARAM);

        long id = pap.query().graph().getNodeId(source);
        Collection<Association> associations = pap.query().graph().getAssociationsWithSource(id);
        List<Map<String, Object>> associationValues = new ArrayList<>();
        for (Association association : associations) {
            associationValues.add(objectMapper.convertValue(association, new TypeReference<>() {}));
        }

        return associationValues;
    }
}
