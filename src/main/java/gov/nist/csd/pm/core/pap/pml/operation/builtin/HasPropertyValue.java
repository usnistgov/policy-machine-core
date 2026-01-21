package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class HasPropertyValue extends PMLQueryOperation<Boolean> {

    public static final FormalParameter<String> VALUE_PARAM = new FormalParameter<>("value", STRING_TYPE);

    public HasPropertyValue() {
        super(
                "hasPropertyValue",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, Env.KEY_PARAM, VALUE_PARAM)
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(Env.KEY_PARAM);
        String value = args.get(VALUE_PARAM);
        Node node = query.graph().getNodeByName(nodeName);
        boolean has = node.getProperties().containsKey(key);
        if (!has) {
            return false;
        }

        return node.getProperties().get(key).equals(value);
    }
}
