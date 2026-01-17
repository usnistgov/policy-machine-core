package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction.NODE_NAME_PARAM;
import static gov.nist.csd.pm.core.pap.pml.function.basic.builtin.Env.KEY_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.node.Node;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class HasPropertyKey extends PMLQueryFunction<Boolean> {

    public HasPropertyKey() {
        super("hasPropertyKey",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM, KEY_PARAM)
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String nodeName = args.get(NODE_NAME_PARAM);
        String key = args.get(KEY_PARAM);
        Node node = query.graph().getNodeByName(nodeName);
        return node.getProperties().containsKey(key);
    }
}
