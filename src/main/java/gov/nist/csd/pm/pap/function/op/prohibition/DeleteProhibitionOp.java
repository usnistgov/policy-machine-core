package gov.nist.csd.pm.pap.function.op.prohibition;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.prohibition.ProhibitionOp.ProhibitionOpArgs;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DELETE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.pap.admin.AdminAccessRights.DELETE_PROHIBITION;

public class DeleteProhibitionOp extends ProhibitionOp<ProhibitionOpArgs> {

    public DeleteProhibitionOp() {
        super(
            "delete_prohibition",
            List.of(NAME_PARAM),
            DELETE_PROCESS_PROHIBITION,
            DELETE_PROHIBITION
        );
    }

    @Override
    protected ProhibitionOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        return new ProhibitionOpArgs(name);
    }

    @Override
    public Void execute(PAP pap, ProhibitionOpArgs args) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
            args.getName()
        );
        return null;
    }
}
