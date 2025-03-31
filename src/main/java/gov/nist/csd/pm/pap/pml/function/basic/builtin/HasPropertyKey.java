package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Env.KEY_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class HasPropertyKey extends PMLBasicFunction {

    public HasPropertyKey() {
        super("hasPropertyKey",
                Type.bool(),
                List.of(NODE_NAME_ARG, KEY_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_ARG).getStringValue();
        String key = args.get(KEY_ARG).getStringValue();
        Node node = pap.query().graph().getNodeByName(nodeName);
        boolean hasPropertyKey = node.getProperties().containsKey(key);
        return new BoolValue(hasPropertyKey);
    }
}
