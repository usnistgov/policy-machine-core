package gov.nist.csd.pm.core.pap.function.op.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.DELETE_PROHIBITION_WITH_COMPLEMENT_CONTAINER;

public class DeleteProhibitionOp extends ProhibitionOp {

    public DeleteProhibitionOp() {
        super(
            "delete_prohibition",
            List.of(NAME_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, ProhibitionOpArgs args) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
            args.getName()
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, ProhibitionOpArgs args) throws PMException {
        checkSubject(pap, userCtx, args.getSubject(), DELETE_PROHIBITION, DELETE_PROCESS_PROHIBITION);
        checkContainers(pap, userCtx, args.getContainers(), DELETE_PROHIBITION, DELETE_PROHIBITION_WITH_COMPLEMENT_CONTAINER);
    }
}
