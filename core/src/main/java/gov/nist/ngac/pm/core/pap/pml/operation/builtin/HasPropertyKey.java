package gov.nist.ngac.pm.core.pap.pml.operation.builtin;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.ngac.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;


/**
 * A PML built-in query that returns whether a node has a given property key.
 */
public class HasPropertyKey extends PMLQueryOperation<Boolean> {

    public HasPropertyKey() {
        super("has_property_key",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, Env.KEY_PARAM),
            List.of()
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(Env.KEY_PARAM);
        Node node = query.graph().getNodeByName(nodeName);
        return node.getProperties().containsKey(key);
    }
}
