package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.op.obligation.ObligationOp.ObligationOpArgs;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_OBLIGATION;

public class DeleteObligationOp extends ObligationOp<ObligationOpArgs> {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            List.of(NAME_PARAM),
            DELETE_OBLIGATION
        );
    }

    @Override
    protected ObligationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_PARAM, argsMap);
        return new ObligationOpArgs(name);
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        pap.modify().obligations().deleteObligation(args.getName());
        return null;
    }
}
