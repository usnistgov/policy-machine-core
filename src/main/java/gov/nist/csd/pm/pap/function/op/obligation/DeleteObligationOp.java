package gov.nist.csd.pm.pap.function.op.obligation;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.op.obligation.ObligationOp.ObligationOpArgs;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.AdminAccessRights.DELETE_OBLIGATION;

public class DeleteObligationOp extends ObligationOp<ObligationOpArgs> {

    public DeleteObligationOp() {
        super(
            "delete_obligation",
            List.of(NAME_ARG),
            DELETE_OBLIGATION
        );
    }

    @Override
    public ObligationOpArgs prepareArgs(Map<FormalParameter<?>, Object> argsMap) {
        String name = prepareArg(NAME_ARG, argsMap);
        return new ObligationOpArgs(name);
    }

    @Override
    public Void execute(PAP pap, ObligationOpArgs args) throws PMException {
        pap.modify().obligations().deleteObligation(args.getName());
        return null;
    }
}
