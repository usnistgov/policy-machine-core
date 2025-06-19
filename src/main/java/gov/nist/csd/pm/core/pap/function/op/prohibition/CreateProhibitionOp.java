package gov.nist.csd.pm.core.pap.function.op.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_PROCESS_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_PROHIBITION;
import static gov.nist.csd.pm.core.pap.admin.AdminAccessRights.CREATE_PROHIBITION_WITH_COMPLEMENT_CONTAINER;

public class CreateProhibitionOp extends ProhibitionOp {

    public CreateProhibitionOp() {
        super(
            "create_prohibition",
            List.of(NAME_PARAM, SUBJECT_PARAM, ARSET_PARAM, INTERSECTION_PARAM, CONTAINERS_PARAM)
        );
    }

    @Override
    public Void execute(PAP pap, ProhibitionOpArgs args) throws PMException {
        pap.modify().prohibitions().createProhibition(
            args.getName(),
            args.getSubject(),
            args.getArset(),
            args.getIntersection(),
            args.getContainers()
        );
        return null;
    }

    @Override
    public void canExecute(PAP pap, UserContext userCtx, ProhibitionOpArgs args) throws PMException {
        checkSubject(pap, userCtx, args.getSubject(), CREATE_PROHIBITION, CREATE_PROCESS_PROHIBITION);
        checkContainers(pap, userCtx, args.getContainers(), CREATE_PROHIBITION, CREATE_PROHIBITION_WITH_COMPLEMENT_CONTAINER);
    }
}
