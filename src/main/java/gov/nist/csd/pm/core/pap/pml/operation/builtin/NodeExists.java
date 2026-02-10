package gov.nist.csd.pm.core.pap.pml.operation.builtin;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;
import static gov.nist.csd.pm.core.pap.pml.operation.basic.PMLFunctionOperation.NODE_NAME_PARAM;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;


public class NodeExists extends PMLQueryOperation<Boolean> {

    public NodeExists() {
        super(
                "nodeExists",
                BOOLEAN_TYPE,
                List.of(NODE_NAME_PARAM),
            List.of()
        );
    }

    @Override
    public Boolean execute(PolicyQuery query, Args args) throws PMException {
        String value = args.get(NODE_NAME_PARAM);
        return query.graph().nodeExists(value);
    }
}
