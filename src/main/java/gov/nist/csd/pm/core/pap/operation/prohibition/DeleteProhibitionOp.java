package gov.nist.csd.pm.core.pap.operation.prohibition;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.reqcap.RequiredCapabilityFunc;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.List;

public class DeleteProhibitionOp extends ProhibitionOp {

    public DeleteProhibitionOp() {
        super(
            "delete_prohibition",
            List.of(NAME_PARAM),
            new RequiredCapabilityFunc((policyQuery, userCtx, args) -> {
                String name = args.get(NAME_PARAM);
                Prohibition prohibition = policyQuery.prohibitions().getProhibition(name);

                return
                    checkSubject(policyQuery, userCtx, prohibition.getSubject(),
                        AdminAccessRight.ADMIN_PROHIBITION_SUBJECT_DELETE)
                        &&
                        checkContainers(policyQuery, userCtx, prohibition.getContainers(),
                            AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_DELETE,
                            AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_DELETE
                        );
            })
        );
    }

    @Override
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().prohibitions().deleteProhibition(
            args.get(NAME_PARAM)
        );
        return null;
    }
}
