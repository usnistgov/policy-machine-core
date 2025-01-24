package gov.nist.csd.pm.common.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.common.obligation.Rule;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBLIGATION;

public class CreateObligationOp extends ObligationOp {

    public CreateObligationOp() {
        super("create_obligation", List.of(AUTHOR_ID_OPERAND, NAME_OPERAND, RULES_OPERAND), CREATE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().obligations().createObligation(
                (long) operands.get(AUTHOR_ID_OPERAND),
                (String) operands.get(NAME_OPERAND),
                (List<Rule>) operands.get(RULES_OPERAND)
        );

        return null;
    }
}
