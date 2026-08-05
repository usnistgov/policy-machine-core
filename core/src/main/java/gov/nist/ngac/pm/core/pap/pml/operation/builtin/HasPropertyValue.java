package gov.nist.ngac.pm.core.pap.pml.operation.builtin;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;


/**
 * PML built-in query has_property_value(node, key, value): returns whether the node has the given
 * property key set to the given value.
 */
public class HasPropertyValue extends PMLQueryOperation<Boolean> {

    public static final FormalParameter<String> VALUE_PARAM = new FormalParameter<>("value", STRING_TYPE);

    public HasPropertyValue() {
        super(
                "has_property_value",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, Env.KEY_PARAM, VALUE_PARAM),
            List.of()
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
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
