package gov.nist.csd.pm.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;
import java.util.Map;

public class GetNodeProperties extends PMLBasicFunction {

    private static final ArgType<Map<String, String>> returnType = mapType(STRING_TYPE, STRING_TYPE);


    public GetNodeProperties() {
        super(
                "getNodeProperties",
                returnType,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        Node node = pap.query().graph().getNodeByName(args.get(NODE_NAME_PARAM));
        return node.getProperties();
    }
}
