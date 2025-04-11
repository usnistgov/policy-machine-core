package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.ArgType.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Env.KEY_ARG;

import com.google.protobuf.BoolValue;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.MapArgs;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;



import java.util.List;


public class HasPropertyKey extends PMLBasicFunction {

    public HasPropertyKey() {
        super("hasPropertyKey",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_ARG, KEY_ARG)
        );
    }

    @Override
    public Object execute(PAP pap, MapArgs args) throws PMException {
        String nodeName = args.get(NODE_NAME_ARG);
        String key = args.get(KEY_ARG);
        Node node = pap.query().graph().getNodeByName(nodeName);
        return node.getProperties().containsKey(key);
    }
}
