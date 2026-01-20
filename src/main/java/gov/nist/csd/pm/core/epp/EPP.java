package gov.nist.csd.pm.core.epp;


import gov.nist.csd.pm.core.common.event.EventContext;
import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OnPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.PDP;
import gov.nist.csd.pm.core.pdp.PDPTx;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class EPP implements EventSubscriber {

    private final PAP pap;
    private final PDP pdp;

    public EPP(PDP pdp, PAP pap) {
        this.pap = pap;
        this.pdp = pdp;
    }

    @Override
    public void processEvent(EventContext eventCtx) throws PMException {
        // operate on a snapshot of the obligations so that if an obligation is added or removed in the response of
        // a matched obligation, it does not get processed in this invocation
        Collection<Obligation> obligations = new ArrayList<>(pap.query().obligations().getObligations());
        for (Obligation obligation : obligations) {
            long author = obligation.getAuthorId();
            UserContext authorCtx = new UserContext(author);

            pdp.runTx(authorCtx, pdpTx -> {
                if (!matches(authorCtx, pdpTx, eventCtx, obligation.getEventPattern())) {
                    return null;
                }

                // execute the obligation response as the stored author
                pdpTx.executeObligationResponse(eventCtx, obligation.getResponse());

                return null;
            });

        }
    }

    protected boolean matches(UserContext userCtx, PDPTx pdpTx, EventContext eventCtx, EventPattern eventPattern) throws PMException {
        return eventPattern.getSubjectPattern().matches(eventCtx.user(), pdpTx.query()) &&
            operationMatches(userCtx, pdpTx, eventCtx.opName(), eventCtx.args(), eventPattern);
    }

    private boolean operationMatches(UserContext userCtx,
                                     PDPTx pdpTx,
                                     String opName,
                                     Map<String, Object> args,
                                     EventPattern eventPattern) throws PMException {
        OperationPattern operationPattern = eventPattern.getOperationPattern();
        if (operationPattern instanceof AnyOperationPattern) {
            return true;
        }

        MatchesOperationPattern matchesOpPattern = (MatchesOperationPattern) operationPattern;

        return opName.equals(matchesOpPattern.getOpName()) &&
            argsMatch(userCtx, pdpTx, args, matchesOpPattern.getOnPattern());
    }

    private boolean argsMatch(UserContext userCtx,
                              PDPTx pdpTx,
                              Map<String, Object> rawArgs,
                              OnPattern onPattern) throws PMException {
        PMLStmtsRoutine<Boolean> matchFunc = onPattern.func();

        // need to pas only the args that the matching function expects which could be a subset of the
        // params defined in the function and passed in rawArgs
        List<FormalParameter<?>> formalParameters = matchFunc.getFormalParameters();
        HashMap<String, Object> matchFuncArgs = new HashMap<>(rawArgs);
        Set<String> paramNames = formalParameters.stream().map(FormalParameter::getName).collect(Collectors.toSet());
        matchFuncArgs.keySet().removeIf(argName -> !paramNames.contains(argName));

        Args args = matchFunc.validateAndPrepareSubsetArgs(matchFuncArgs);

        // first, check the user has any privileges on each node in the event context args - any privilege works
        checkAccessOnEventContextArgs(userCtx, args.getMap());

        // execute the matching function to determine if event context args match the pattern
        // use the pdptx so that any calls to the querier have privilege checks
        ExecutionContext executionContext = pdpTx.buildExecutionContext(userCtx);
        matchFunc.setCtx(executionContext);
        return (boolean) pdpTx.executeOperation(matchFunc, args);
    }

    private void checkAccessOnEventContextArgs(UserContext userCtx,
                                               Map<FormalParameter<?>, Object> argsMap) throws PMException {
        for (Entry<FormalParameter<?>, Object> entry : argsMap.entrySet()) {
            FormalParameter<?> formalParameter = entry.getKey();
            Object value = entry.getValue();

            switch (formalParameter) {
                case NodeIdFormalParameter nodeId ->
                    pap.privilegeChecker().check(userCtx, new TargetContext((long) value));
                case NodeIdListFormalParameter nodeIdList -> {
                    List<Long> idList = (List<Long>) value;
                    for (Long id : idList) {
                        pap.privilegeChecker().check(userCtx, new TargetContext(id));
                    }
                }
                case NodeNameFormalParameter nodeName ->
                    pap.privilegeChecker().check(userCtx, new TargetContext(pap.query().graph().getNodeId((String) value)));
                case NodeNameListFormalParameter nodeNameList -> {
                    List<String> nameList = (List<String>) value;
                    for (String name : nameList) {
                        pap.privilegeChecker().check(userCtx, new TargetContext(pap.query().graph().getNodeId(name)));
                    }
                }
                default -> {
                    return;
                }
            }
        }
    }
}
