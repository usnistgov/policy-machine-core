package gov.nist.csd.pm.core.pap.function.op.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;
import gov.nist.csd.pm.core.common.prohibition.ContainerCondition;
import gov.nist.csd.pm.core.pap.PAP;

import gov.nist.csd.pm.core.pap.function.arg.Args;
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
        checkSubject(pap, userCtx, args.get(SUBJECT_PARAM), CREATE_PROHIBITION, CREATE_PROCESS_PROHIBITION);
        checkContainers(pap, userCtx, args.get(CONTAINERS_PARAM), CREATE_PROHIBITION, CREATE_PROHIBITION_WITH_COMPLEMENT_CONTAINER);
    }
}
