package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;

public class GetNodeType extends PMLBasicFunction {

    public GetNodeType() {
        super(
                "getNodeType",
                Type.string(),
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        Node node = pap.query().graph().getNodeByName(args.get(NODE_NAME_ARG).getStringValue());
        return new StringValue(node.getType().toString());
    }
}

