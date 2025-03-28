package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.ActualArgs;

import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_PROHIBITION;

public class CreateProhibitionOp extends ProhibitionOp {

    public CreateProhibitionOp() {
        super("create_prohibition", CREATE_PROCESS_PROHIBITION, CREATE_PROHIBITION);
    }

    @Override
    public Void execute(PAP pap, ActualArgs operands) throws PMException {
        pap.modify().prohibitions().createProhibition(
                operands.get(NAME_ARG),
                operands.get(SUBJECT_ARG),
                operands.get(ARSET_ARG),
                operands.get(INTERSECTION_ARG),
                operands.get(CONTAINERS_ARG)
        );
        return null;
    }
}
