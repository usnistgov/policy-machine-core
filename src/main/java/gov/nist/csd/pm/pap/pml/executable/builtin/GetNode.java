package gov.nist.csd.pm.pap.pml.executable.builtin;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.executable.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class GetNode extends PMLFunction {

    public GetNode() {
        super(
                "getNode",
                Type.map(Type.string(), Type.any()),
                List.of(NODE_NAME_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        Node node = pap.query().graph().getNodeByName(actualArgs.get(NODE_NAME_ARG).getStringValue());

        return Value.fromObject(node);
    }
}
