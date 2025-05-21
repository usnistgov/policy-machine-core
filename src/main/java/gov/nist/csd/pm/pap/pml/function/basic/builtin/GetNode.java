package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;
import java.util.Map;


public class GetNode extends PMLBasicFunction {

    public GetNode() {
        super(
                "getNode",
                MapType.of(STRING_TYPE, ANY_TYPE),
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Map<String, Object> execute(PAP pap, Args args) throws PMException {
        Node node = pap.query().graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return Map.of(
            "name", node.getName(),
            "type", node.getType().toString(),
            "properties", node.getProperties()
        );
    }
}
