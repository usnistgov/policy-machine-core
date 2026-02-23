package gov.nist.csd.pm.core.epp;


import gov.nist.csd.pm.core.common.event.EventSubscriber;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OnPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.csd.pm.core.pap.operation.Operation;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.arg.Args;
import gov.nist.csd.pm.core.pap.operation.param.FormalParameter;
import gov.nist.csd.pm.core.pap.operation.param.NodeFormalParameter;
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
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        if (!opName.equals(matchesOpPattern.getOpName())) {
            return false;
        }

        return argsMatch(userCtx, pdpTx, opName, args, matchesOpPattern.getOnPattern());
    }

    private boolean argsMatch(UserContext userCtx,
                              PDPTx pdpTx,
                              String opName,
                              Map<String, Object> rawArgs,
                              OnPattern onPattern) throws PMException {
        PMLStmtsRoutine<Boolean> matchFunc = onPattern.func();

        // get the matching functions corresponding operation and ensure that the provided
        // args are a subset of the defined event parameters
        Operation<?> matchFuncOp = pap.query().operations().getOperation(opName);
        Args args = matchFuncOp.validateEventContextArgs(rawArgs);

        // first, check the user has any privileges on each node in the event context args - any privilege works
        checkAccessOnEventContextArgs(userCtx, args.getMap());

        // execute the matching operation to determine if event context args match the pattern
        // use the pdptx so that any calls to the querier have privilege checks
        ExecutionContext executionContext = pdpTx.buildExecutionContext(userCtx);
        matchFunc.setCtx(executionContext);
        return (boolean) pdpTx.executeOperation(matchFunc, args);
    }

    private void checkAccessOnEventContextArgs(UserContext userCtx,
                                               Map<FormalParameter<?>, Object> argsMap) throws PMException {
        for (Entry<FormalParameter<?>, Object> entry : argsMap.entrySet()) {
            FormalParameter<?> formalParameter = entry.getKey();
            if (!(formalParameter instanceof NodeFormalParameter<?> nodeFormalParameter)) {
                continue;
            }

            Object value = entry.getValue();

            switch (nodeFormalParameter) {
                case NodeIdFormalParameter nodeId ->
                    check(userCtx, new TargetContext((long) value));
                case NodeIdListFormalParameter nodeIdList -> {
                    List<Long> idList = (List<Long>) value;
                    for (Long id : idList) {
                        check(userCtx, new TargetContext(id));
                    }
                }
                case NodeNameFormalParameter nodeName ->
                    check(userCtx, new TargetContext(pap.query().graph().getNodeId((String) value)));
                case NodeNameListFormalParameter nodeNameList -> {
                    List<String> nameList = (List<String>) value;
                    for (String name : nameList) {
                        check(userCtx, new TargetContext(pap.query().graph().getNodeId(name)));
                    }
                }
            }
        }
    }

    private void check(UserContext userCtx, TargetContext targetCtx) throws PMException {
        if(pap.query()
            .access()
            .computePrivileges(userCtx, targetCtx)
            .isEmpty()) {
            throw UnauthorizedException.of(pap.query().graph(), userCtx, targetCtx, new AccessRightSet(), List.of());
        }
    }
}
