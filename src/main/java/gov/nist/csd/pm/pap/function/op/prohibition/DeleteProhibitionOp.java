package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.Args;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_PROHIBITION;

public class DeleteProhibitionOp extends ProhibitionOp {

    public DeleteProhibitionOp() {
        super("delete_prohibition", DELETE_PROCESS_PROHIBITION, DELETE_PROHIBITION);
    }

    @Override
    public Void execute(PAP pap, Args operands) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
                operands.get(NAME_ARG)
        );
        return null;
    }
}
