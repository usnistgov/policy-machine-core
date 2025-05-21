package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Env.KEY_PARAM;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;


public class HasPropertyKey extends PMLBasicFunction {

    public HasPropertyKey() {
        super("hasPropertyKey",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, KEY_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(KEY_PARAM);
        Node node = pap.query().graph().getNodeByName(nodeName);
        return node.getProperties().containsKey(key);
    }
}
