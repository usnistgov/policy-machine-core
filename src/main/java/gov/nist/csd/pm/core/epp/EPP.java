package gov.nist.csd.pm.core.epp;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.ANY_TYPE;
import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;


import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.Response;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;

import gov.nist.csd.pm.core.pdp.PDPTx;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EPP implements EventSubscriber {

    private final PAP pap;
    private final PDP pdp;

    public EPP(PDP pdp, PAP pap) {
        this.pap = pap;
        this.pdp = pdp;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        Collection<Obligation> obligations = pap.query().obligations().getObligations();
        for(Obligation obligation : obligations) {
            long author = obligation.getAuthorId();
            List<Rule> rules = obligation.getRules();
            for(Rule rule : rules) {
                if(!rule.getEventPattern().matches(eventCtx, pap)) {
                    continue;
                }

                UserContext authorCtx = new UserContext(author);
                Response response = rule.getResponse();

                executeResponse(authorCtx, response, eventCtx);
            }
        }
    }

    public void executeResponse(UserContext user, Response response, EventContext eventCtx) throws PMException {
        pdp.runTx(user, pdpTx -> {
            executeResponse(pdpTx, user, eventCtx, response);
            return null;
        });
    }

    private void executeResponse(PDPTx pdpTx, UserContext userCtx, EventContext eventCtx, Response response) throws PMException {
        Args args = new Args();

        FormalParameter<Map<String, Object>> eventCtxParam = new FormalParameter<>(
            response.getEventCtxVariable(),
            MapType.of(STRING_TYPE, ANY_TYPE)
        );

        args.put(eventCtxParam, eventCtxToMap(eventCtx));

        ExecutionContext executionCtx = pdpTx.buildExecutionContext(userCtx);
        executionCtx.executeStatements(response.getStatements(), args);
    }

    private Map<String, Object> eventCtxToMap(EventContext eventCtx) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", eventCtx.user().getName());
        map.put("attrs", eventCtx.user().getAttrs());
        map.put("process", eventCtx.user().getProcess());
        map.put("opName", eventCtx.opName());
        map.put("args", eventCtx.args());

        return map;
    }
}
