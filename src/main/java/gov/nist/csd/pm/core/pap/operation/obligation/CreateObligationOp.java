package gov.nist.csd.pm.core.pap.operation.obligation;

import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.VOID_TYPE;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.admin.AdminPolicyNode;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.operation.AdminOperation;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.arg.type.EventPatternType;
import gov.nist.csd.pm.core.pap.operation.arg.type.ObligationResponseType;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import java.util.List;

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
    public Void execute(PAP pap, Args args) throws PMException {
        pap.modify().obligations().createObligation(
            args.get(AUTHOR_PARAM),
            args.get(NAME_PARAM),
            args.get(EVENT_PATTERN_PARAM),
            args.get(OBLIGATION_RESPONSE_PARAM)
        );
        return null;
    }
}
