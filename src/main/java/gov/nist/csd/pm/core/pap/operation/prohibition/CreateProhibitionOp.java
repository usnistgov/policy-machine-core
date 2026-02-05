package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class CreateProhibitionOp extends ProhibitionOp {

    public CreateProhibitionOp() {
        super(
            "create_prohibition",
            List.of(NAME_PARAM, SUBJECT_PARAM, ARSET_PARAM, INTERSECTION_PARAM, CONTAINERS_PARAM),

            new RequiredCapabilityFunc((policyQuery, userCtx, args) ->
                checkSubject(policyQuery, userCtx, args.get(SUBJECT_PARAM),
                    AdminAccessRight.ADMIN_PROHIBITION_SUBJECT_CREATE)
                    &&
                    checkContainers(policyQuery, userCtx, args.get(CONTAINERS_PARAM),
                        AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_CREATE,
                        AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_CREATE
                    ))
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
}
