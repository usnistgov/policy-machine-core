package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteProhibitionOp extends ProhibitionOp {

    public DeleteProhibitionOp() {
        super(
            "delete_prohibition",
            List.of(NAME_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
            args.get(NAME_PARAM)
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        checkSubject(pap, userCtx, args.get(SUBJECT_PARAM),
            AdminAccessRight.ADMIN_PROHIBITION_DELETE.toString(),
            AdminAccessRight.ADMIN_PROHIBITION_PROCESS_DELETE.toString());
        checkContainers(pap, userCtx, args.get(CONTAINERS_PARAM),
            AdminAccessRight.ADMIN_PROHIBITION_DELETE.toString(),
            AdminAccessRight.ADMIN_PROHIBITION_COMPLEMENT_CONTAINER_DELETE.toString());
    }
}
