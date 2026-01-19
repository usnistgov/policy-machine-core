package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;


import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.pml.function.basic.PMLBasicFunction.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class NodeExists extends PMLQueryFunction<Boolean> {

    public NodeExists() {
        super(
                "nodeExists",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM)
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String value = args.get(NODE_NAME_PARAM);
        return query.graph().nodeExists(value);
    }
}
