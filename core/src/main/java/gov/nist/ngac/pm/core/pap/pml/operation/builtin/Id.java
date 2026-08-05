package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.pml.operation.query.PMLQueryOperation;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
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
    public Long execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        String name = args.get(NAME_PARAM);
        return query.graph().getNodeId(name);
    }
}
