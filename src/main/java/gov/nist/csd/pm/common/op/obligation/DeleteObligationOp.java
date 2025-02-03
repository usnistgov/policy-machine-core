package gov.nist.csd.pm.common.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;

import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_OBLIGATION;

public class DeleteObligationOp extends ObligationOp {

    public DeleteObligationOp() {
        super("delete_obligation", DELETE_OBLIGATION);
    }

    @Override
    public Void execute(PAP pap, Map<String, Object> operands) throws PMException {
        pap.modify().obligations().deleteObligation((String) operands.get(NAME_OPERAND));

        return null;
    }
}
