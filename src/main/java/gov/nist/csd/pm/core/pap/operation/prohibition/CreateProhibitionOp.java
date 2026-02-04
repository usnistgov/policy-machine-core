package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateProhibitionOp extends ProhibitionOp {

    public CreateProhibitionOp() {
        super(
            "create_prohibition",
            List.of(NAME_PARAM, SUBJECT_PARAM, ARSET_PARAM, INTERSECTION_PARAM, CONTAINERS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().createProhibition(
            args.get(NAME_PARAM),
            args.get(SUBJECT_PARAM),
            new AccessRightSet(args.get(ARSET_PARAM)),
            args.get(INTERSECTION_PARAM),
            args.get(CONTAINERS_PARAM)
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, Args args) throws PMException {
        checkSubject(pap, userCtx, args.get(SUBJECT_PARAM), AdminAccessRight.ADMIN_PROHIBITION_CREATE.toString(),
            AdminAccessRight.ADMIN_PROHIBITION_PROCESS_CREATE.toString());
        checkContainers(pap, userCtx, args.get(CONTAINERS_PARAM), AdminAccessRight.ADMIN_PROHIBITION_CREATE.toString(),
            AdminAccessRight.ADMIN_PROHIBITION_COMPLEMENT_CONTAINER_CREATE.toString());
    }
}
