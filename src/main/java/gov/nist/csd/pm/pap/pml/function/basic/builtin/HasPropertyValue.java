package gov.nist.csd.pm.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.pap.function.arg.type.Type.BOOLEAN_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.pml.function.basic.builtin.Env.KEY_PARAM;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.graph.node.Node;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.pml.function.basic.PMLBasicFunction;


import java.util.List;


public class HasPropertyValue extends PMLBasicFunction {

    public static final FormalParameter<String> VALUE_PARAM = new FormalParameter<>("value", STRING_TYPE);

    public HasPropertyValue() {
        super(
                "hasPropertyValue",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, KEY_PARAM, VALUE_PARAM)
        );
    }

    @Override
    public Object execute(PAP pap, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(KEY_PARAM);
        String value = args.get(VALUE_PARAM);
        Node node = pap.query().graph().getNodeByName(nodeName);
        boolean has = node.getProperties().containsKey(key);
        if (!has) {
            return false;
        }

        return node.getProperties().get(key).equals(value);
    }
}
