package gov.nist.csd.pm.epp;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.ANY_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.event.EventSubscriber;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.obligation.Obligation;
import gov.nist.csd.pm.pap.obligation.Response;
import gov.nist.csd.pm.pap.obligation.Rule;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pdp.PDP;

import gov.nist.csd.pm.pdp.PDPTx;
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

                // need to run pdp tx as author of obligation
                pdp.runTx(authorCtx, txPDP -> {
                    executeResponse(txPDP, authorCtx, eventCtx, response);
                    return null;
                });
            }
        }
    }

    public void executeResponse(PDPTx pdpTx, UserContext userCtx, EventContext eventCtx, Response response) throws PMException {
        Args args = new Args();

        FormalParameter<Map<String, Object>> eventCtxParam = new FormalParameter<>(
            response.getEventCtxVariable(),
            mapType(STRING_TYPE, ANY_TYPE)
        );

        args.put(eventCtxParam, eventCtxToMap(eventCtx));

        ExecutionContext executionCtx = pdpTx.buildExecutionContext(userCtx);
        executionCtx.executeStatements(response.getStatements(), args);
    }

    private Map<String, Object> eventCtxToMap(EventContext eventCtx) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", eventCtx.getUser());
        map.put("opName", eventCtx.getOpName());
        map.put("args", eventCtx.getArgs());
        if (eventCtx.getProcess() != null) {
            map.put("process", eventCtx.getUser());
        }

        return map;
    }
}
