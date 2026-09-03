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

package gov.nist.ngac.pm.core.epp;


import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.BOOLEAN_TYPE;

import gov.nist.ngac.pm.core.common.event.EventSubscriber;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.obligation.event.EventContextUser;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.OnPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.operation.Operation;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.arg.Args;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeIdListFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameListFormalParameter;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLRoutine;
import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.pdp.PDPTx;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Event Processing Point that processes event contexts against obligations.
 */
public class EPP implements EventSubscriber {

    private static final Logger logger = LoggerFactory.getLogger(EPP.class);

    private final PAP pap;
    private final PDP pdp;

    public EPP(PDP pdp, PAP pap) {
        this.pap = pap;
        this.pdp = pdp;
    }

    @Override
    public void processEvent(EventContext eventCtx) {
        // operate on a snapshot of the obligations so that if an obligation is added or removed in the response of
        // a matched obligation, it does not get processed in this invocation
        Collection<Obligation> obligations = new ArrayList<>();
        try {
            obligations = new ArrayList<>(pap.query().obligations().getObligations());
        } catch (PMException e) {
            logger.error("error retrieving obligations", e);
        }

        for (Obligation obligation : obligations) {
            NodeUserContext authorCtx = obligation.getAuthor();

            try {
                pdp.runTx(authorCtx, pdpTx -> {
                    // create an execution context with the event context in scope - all other vars and ops are pre loaded
                    ExecutionContext executionContext = pdpTx.buildExecutionContext(authorCtx);
                    executionContext.scope().addVariable("ctx", eventCtx.toMap());

                    // pass a copy of the execution context so the scope does not propagate
                    if (!matches(authorCtx, pdpTx, eventCtx, executionContext.copy(), obligation.getEventPattern())) {
                        return null;
                    }

                    // execute the obligation response as the stored author
                    pdpTx.executeObligationResponse(eventCtx, executionContext.copy(), obligation.getResponse());

                    return null;
                });
            } catch (Exception e) {
                logger.error("error processing event context {} with obligation {}", eventCtx, obligation.getName(), e);
            }
        }
    }

    protected boolean matches(UserContext userCtx,
                              PDPTx pdpTx,
                              EventContext eventCtx,
                              ExecutionContext executionContext,
                              EventPattern eventPattern) throws PMException {
        return subjectMatches(pdpTx, executionContext, eventCtx.user(), eventPattern.getSubjectPattern()) &&
            operationMatches(userCtx, pdpTx, executionContext, eventCtx.opName(), eventCtx.args(), eventPattern.getOperationPattern());
    }

    private boolean subjectMatches(PDPTx pdpTx,
                                   ExecutionContext executionContext,
                                   EventContextUser eventContextUser,
                                   SubjectPattern subjectPattern) throws PMException {
        PMLRoutine<?> routine = new PMLRoutine<>("subject_matches", BOOLEAN_TYPE, List.of()) {
            @Override
            public Boolean execute(PAP pap, UserContext userCtx, Args args) throws PMException {
                return subjectPattern.matches(eventContextUser, executionContext, pap);
            }
        };

        routine.setCtx(executionContext);
        return (boolean) pdpTx.executeOperation(routine, executionContext.author(), new Args());
    }

    private boolean operationMatches(UserContext userCtx,
                                     PDPTx pdpTx,
                                     ExecutionContext executionContext,
                                     String opName,
                                     Map<String, Object> args,
                                     OperationPattern operationPattern) throws PMException {
        if (operationPattern instanceof AnyOperationPattern) {
            return true;
        }

        MatchesOperationPattern matchesOpPattern = (MatchesOperationPattern) operationPattern;
        if (!opName.equals(matchesOpPattern.getOpName())) {
            return false;
        }

        return argsMatch(userCtx, pdpTx, executionContext, opName, args, matchesOpPattern.getOnPattern());
    }

    private boolean argsMatch(UserContext userCtx,
                              PDPTx pdpTx,
                              ExecutionContext executionContext,
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
        matchFunc.setCtx(executionContext);
        return (boolean) pdpTx.executeOperation(matchFunc, executionContext.author(), args);
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
                    check(userCtx, NodeTargetContext.of((long) value));
                case NodeIdListFormalParameter nodeIdList -> {
                    List<Long> idList = (List<Long>) value;
                    for (Long id : idList) {
                        check(userCtx, NodeTargetContext.of(id));
                    }
                }
                case NodeNameFormalParameter nodeName ->
                    check(userCtx, NodeTargetContext.of(pap.query().graph().getNodeId((String) value)));
                case NodeNameListFormalParameter nodeNameList -> {
                    List<String> nameList = (List<String>) value;
                    for (String name : nameList) {
                        check(userCtx, NodeTargetContext.of(pap.query().graph().getNodeId(name)));
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
