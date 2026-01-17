package gov.nist.csd.pm.core.pap.pml.function.basic.builtin;

import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.List;

public class Id extends PMLQueryFunction<Long> {

    public Id() {
        super(
            "id",
            LONG_TYPE,
            List.of(NAME_PARAM)
        );
    }

    @Override
    public Long execute(PolicyQuery query, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return query.graph().getNodeId(name);
    }
}
