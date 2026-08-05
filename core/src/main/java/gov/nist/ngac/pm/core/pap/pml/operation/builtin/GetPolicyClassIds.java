package gov.nist.ngac.pm.core.pap.pml.operation.builtin;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.QueryOperation;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ListType;
import gov.nist.ngac.pm.core.pap.query.PolicyQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetPolicyClassIds extends QueryOperation<List<Long>> {

    public GetPolicyClassIds() {
        super(
            "get_policy_class_ids",
            ListType.of(LONG_TYPE),
            List.of(),
            List.of()
        );
    }

    @Override
    public List<Long> execute(PolicyQuery query, UserContext userCtx, Args args) throws PMException {
        Collection<Long> policyClasses = query.graph().getPolicyClasses();
        return new ArrayList<>(policyClasses);
    }
}
