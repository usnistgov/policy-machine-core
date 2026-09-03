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

package gov.nist.ngac.pm.core.pap.operation.obligation;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.operation.AdminOperation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.arg.type.EventPatternType;
import gov.nist.ngac.pm.core.pap.operation.arg.type.ObligationResponseType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.List;

/**
 * Create an obligation.
 */
public class CreateObligationOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter AUTHOR_PARAM =
        new NodeIdFormalParameter("author");
    public static final FormalParameter<EventPattern> EVENT_PATTERN_PARAM =
        new FormalParameter<>("event_pattern", new EventPatternType());
    public static final FormalParameter<ObligationResponse> OBLIGATION_RESPONSE_PARAM =
        new FormalParameter<>("obligation_response", new ObligationResponseType());

    public CreateObligationOp() {
        super(
            "create_obligation",
            VOID_TYPE,
            List.of(AUTHOR_PARAM, NAME_PARAM, EVENT_PATTERN_PARAM, OBLIGATION_RESPONSE_PARAM),
            AdminPolicyNode.PM_ADMIN_OBLIGATIONS,
            AdminAccessRight.ADMIN_OBLIGATION_CREATE
        );
    }

    @Override
    public Void execute(PAP pap, UserContext userCtx, Args args) throws PMException {
        Obligation obligation = new Obligation(
            NodeUserContext.of(args.get(AUTHOR_PARAM)),
            args.get(NAME_PARAM),
            args.get(EVENT_PATTERN_PARAM),
            args.get(OBLIGATION_RESPONSE_PARAM)
        );

        pap.modify().obligations().createObligation(obligation);
        return null;
    }
}
