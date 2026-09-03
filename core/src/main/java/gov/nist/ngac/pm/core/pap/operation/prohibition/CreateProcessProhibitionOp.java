/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation.prohibition;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredCapability;
import gov.nist.ngac.pm.core.pap.operation.reqcap.RequiredPrivilegeOnParameter;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.HashSet;
import java.util.List;

/**
 * Create a new process prohibition.
 */
public class CreateProcessProhibitionOp extends ProhibitionOp {

    public static NodeIdFormalParameter USER_ID_PARAM = new NodeIdFormalParameter("user_id");
    public static FormalParameter<String> PROCESS_PARAM = new FormalParameter<>("process", STRING_TYPE);

    public CreateProcessProhibitionOp() {
        super(
            "create_process_prohibition",
            List.of(NAME_PARAM, USER_ID_PARAM, PROCESS_PARAM, ARSET_PARAM, INCLUSION_SET_PARAM, EXCLUSION_SET_PARAM, IS_CONJUNCTIVE_PARAM),
            new RequiredCapability(
                new RequiredPrivilegeOnParameter(USER_ID_PARAM, AdminAccessRight.ADMIN_PROHIBITION_PROCESS_CREATE),
                new RequiredPrivilegeOnParameter(INCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_INCLUSION_CREATE),
                new RequiredPrivilegeOnParameter(EXCLUSION_SET_PARAM, AdminAccessRight.ADMIN_PROHIBITION_EXCLUSION_CREATE)
            )
        );
    }

    @Override
    public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        pap.modify().prohibitions().createProcessProhibition(
            args.get(NAME_PARAM),
            args.get(USER_ID_PARAM),
            args.get(PROCESS_PARAM),
            new AccessRightSet(args.get(ARSET_PARAM)),
            new HashSet<>(args.get(INCLUSION_SET_PARAM)),
            new HashSet<>(args.get(EXCLUSION_SET_PARAM)),
            args.get(IS_CONJUNCTIVE_PARAM)
        );
        return null;
    }
}
