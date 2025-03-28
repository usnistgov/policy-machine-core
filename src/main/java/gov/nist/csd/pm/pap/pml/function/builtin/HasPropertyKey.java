package gov.nist.csd.pm.pap.pml.function.builtin;


import static gov.nist.csd.pm.pap.pml.function.builtin.Env.KEY_ARG;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;
import gov.nist.csd.pm.pap.pml.function.PMLFunction;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.BoolValue;
import gov.nist.csd.pm.pap.pml.value.Value;

import java.util.List;


public class HasPropertyKey extends PMLFunction {

    public HasPropertyKey() {
        super("hasPropertyKey",
                Type.bool(),
                List.of(NODE_NAME_ARG, KEY_ARG)
        );
    }

    @Override
    public Value execute(PAP pap, ActualArgs actualArgs) throws PMException {
        String nodeName = actualArgs.get(NODE_NAME_ARG).getStringValue();
        String key = actualArgs.get(KEY_ARG).getStringValue();
        Node node = pap.query().graph().getNodeByName(nodeName);
        boolean hasPropertyKey = node.getProperties().containsKey(key);
        return new BoolValue(hasPropertyKey);
    }
}
