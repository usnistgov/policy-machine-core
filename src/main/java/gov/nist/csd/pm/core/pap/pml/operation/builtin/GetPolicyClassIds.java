package gov.nist.csd.pm.core.pap.pml.operation.builtin;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.LONG_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.QueryOperation;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.ListType;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetPolicyClassIds extends QueryOperation<List<Long>> {

    public GetPolicyClassIds() {
        super(
            "getPolicyClassIds",
            ListType.of(LONG_TYPE),
            List.of()
        );
    }

    @Override
    public List<Long> execute(PolicyQuery query, Args args) throws PMException {
        Collection<Long> policyClasses = query.graph().getPolicyClasses();
        return new ArrayList<>(policyClasses);
    }
}
