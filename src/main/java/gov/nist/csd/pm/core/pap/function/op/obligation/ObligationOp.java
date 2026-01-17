package gov.nist.csd.pm.core.pap.function.op.obligation;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.function.arg.type.EventPatternType;
import gov.nist.csd.pm.core.pap.function.arg.type.ObligationResponseType;
import gov.nist.csd.pm.core.pap.function.AdminOperation;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import java.util.List;

public abstract class ObligationOp extends AdminOperation<Void> {

    public static final NodeIdFormalParameter AUTHOR_PARAM = new NodeIdFormalParameter("author");
    public static final FormalParameter<EventPattern> EVENT_PATTERN_PARAM =
        new FormalParameter<>("event_pattern", new EventPatternType());
    public static final FormalParameter<ObligationResponse> OBLIGATION_RESPONSE_PARAM =
        new FormalParameter<>("obligation_response", new ObligationResponseType());

    public ObligationOp(String opName, List<FormalParameter<?>> formalParameters) {
        super(opName, BasicTypes.VOID_TYPE, formalParameters);
    }
}
