package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class HasPropertyKey extends PMLQueryOperation<Boolean> {

    public HasPropertyKey() {
        super("hasPropertyKey",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, Env.KEY_PARAM),
            List.of()
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(Env.KEY_PARAM);
        Node node = query.graph().getNodeByName(nodeName);
        return node.getProperties().containsKey(key);
    }
}
