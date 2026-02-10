package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public class Id extends PMLQueryOperation<Long> {

    public Id() {
        super(
            "id",
            LONG_TYPE,
            List.of(NAME_PARAM),
            List.of()
        );
    }

    @Override
    public Long execute(PolicyQuery query, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return query.graph().getNodeId(name);
    }
}
