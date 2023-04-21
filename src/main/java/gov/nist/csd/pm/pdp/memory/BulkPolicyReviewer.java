package gov.nist.csd.pm.pdp.memory;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.dag.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.events.PolicyEventListener;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.dag.TargetDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.UserDagResult;
import gov.nist.csd.pm.policy.model.graph.dag.propagator.Propagator;
import gov.nist.csd.pm.policy.model.graph.dag.visitor.Visitor;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;

import java.util.*;

public class BulkPolicyReviewer extends MemoryPolicyReviewer {

    private final UserDagEventListener userDagEventListener;
    private final PAP pap;
    private Map<String, Map<String, AccessRightSet>> visitedNodes = new HashMap<>();

    public BulkPolicyReviewer(UserContext userCtx, PAP pap) throws PMException {
        super(pap);
        this.pap = pap;

        this.userDagEventListener = new UserDagEventListener(userCtx, pap);
        pap.addEventListener(userDagEventListener, true);
    }

    @Override
    public synchronized AccessRightSet getAccessRights(UserContext userCtx, String target) throws PMException {
        AccessRightSet accessRights = new AccessRightSet();

        UserDagResult userDagResult = userDagEventListener.userDagResult;

        if (userDagResult.borderTargets().isEmpty()) {
            return accessRights;
        }

        TargetDagResult targetDagResult = processTargetDAG(target, userDagResult);

        return resolvePermissions(userDagResult, targetDagResult, target, pap.getResourceAccessRights());
    }

    @Override
    protected TargetDagResult processTargetDAG(String target, UserDagResult userCtx) throws PMException {
        Map<String, AccessRightSet> borderTargets = userCtx.borderTargets();
        Set<String> reachedTargets = new HashSet<>();

        Visitor visitor = node -> {
            // mark the node as reached, to be used for resolving prohibitions
            if (userCtx.prohibitionTargets().contains(node)) {
                reachedTargets.add(node);
            }

            Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(node, new HashMap<>());
            if (nodeCtx.isEmpty()) {
                visitedNodes.put(node, nodeCtx);
            }

            if (userDagEventListener.policyClasses.contains(node)) {
                nodeCtx.put(node, new AccessRightSet());
            } else if (borderTargets.containsKey(node)) {
                Set<String> uaOps = borderTargets.get(node);
                for (Map.Entry<String, AccessRightSet> pc : nodeCtx.entrySet()) {
                    AccessRightSet pcOps = pc.getValue();
                    pcOps.addAll(uaOps);
                    nodeCtx.put(pc.getKey(), pcOps);
                }
            }
        };

        Propagator propagator = (parent, child) -> {
            Map<String, AccessRightSet> parentCtx = visitedNodes.get(parent);
            Map<String, AccessRightSet> nodeCtx = visitedNodes.getOrDefault(child, new HashMap<>());

            for (Map.Entry<String, AccessRightSet> entry : parentCtx.entrySet()) {
                AccessRightSet ops = nodeCtx.getOrDefault(entry.getKey(), new AccessRightSet());
                ops.addAll(entry.getValue());
                nodeCtx.put(entry.getKey(), ops);
            }

            visitedNodes.put(child, nodeCtx);
        };

        new DepthFirstGraphWalker(pap)
                .withDirection(Direction.PARENTS)
                .withVisitor(visitor)
                .withPropagator(propagator)
                .withSinglePathShortCircuit(node -> visitedNodes.containsKey(node))
                .walk(target);

        Map<String, AccessRightSet> pcMap = visitedNodes.get(target);

        // remove the target to avoid cluttering the visited nodes cache
        visitedNodes.remove(target);

        return new TargetDagResult(pcMap, reachedTargets);
    }

    class UserDagEventListener implements PolicyEventListener {

        private UserContext userCtx;
        private MemoryPolicyReviewer memoryPolicyReviewer;
        private PolicyReader policyReader;
        UserDagResult userDagResult;
        List<String> policyClasses;
        UserDagEventListener(UserContext userContext, PolicyReader policyReader) throws PMException {
            this.userCtx = userContext;
            this.policyReader = policyReader;

            this.memoryPolicyReviewer = new MemoryPolicyReviewer(policyReader);
            this.userDagResult = processUserDAG(userCtx.getUser(), userCtx.getProcess());
            this.policyClasses = policyReader.getPolicyClasses();
        }

        @Override
        public void handlePolicyEvent(PolicyEvent event) throws PMException {
            this.userDagResult = memoryPolicyReviewer.processUserDAG(userCtx.getUser(), userCtx.getProcess());
            this.policyClasses = policyReader.getPolicyClasses();
        }
    }
}
