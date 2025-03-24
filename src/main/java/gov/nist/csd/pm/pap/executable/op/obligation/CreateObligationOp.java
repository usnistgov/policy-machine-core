package gov.nist.csd.pm.pap.executable.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super("create_obligation", CREATE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().obligations().createObligation(
                (long) operands.get(AUTHOR_OPERAND),
                (String) operands.get(NAME_OPERAND),
                (List<Rule>) operands.get(RULES_OPERAND)
        );

        return null;
    }
}
