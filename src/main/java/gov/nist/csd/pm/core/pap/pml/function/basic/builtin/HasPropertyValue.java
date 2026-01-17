package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction.NODE_NAME_PARAM;
import static gov.nist.csd.pm.core.pap.pml.function.basic.builtin.Env.KEY_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class HasPropertyValue extends PMLQueryFunction<Boolean> {

    public static final FormalParameter<String> VALUE_PARAM = new FormalParameter<>("value", STRING_TYPE);

    public HasPropertyValue() {
        super(
                "hasPropertyValue",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, KEY_PARAM, VALUE_PARAM)
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(KEY_PARAM);
        String value = args.get(VALUE_PARAM);
        Node node = query.graph().getNodeByName(nodeName);
        boolean has = node.getProperties().containsKey(key);
        if (!has) {
            return false;
        }

        return node.getProperties().get(key).equals(value);
    }
}
