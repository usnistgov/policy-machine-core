package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.policy.model.graph.dag.walker.dfs.DepthFirstGraphWalker;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.dag.walker.Direction;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.review.GraphReview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static gov.nist.csd.pm.policy.model.graph.nodes.NodeType.*;

public class GraphReviewer implements GraphReview {

    private final Policy policy;

    public GraphReviewer(Policy policy) {
        this.policy = policy;
    }

    @Override
    public List<String> getAttributeContainers(String node) throws PMException {
        List<String> attrs = new ArrayList<>();

        new DepthFirstGraphWalker(policy.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = policy.graph().getNode(n);
                    if (visitedNode.getType().equals(UA) ||
                            visitedNode.getType().equals(OA)) {
                        attrs.add(n);
                    }
                })
                .walk(node);

        return attrs;
    }

    @Override
    public List<String> getPolicyClassContainers(String node) throws PMException {
        List<String> attrs = new ArrayList<>();

        new DepthFirstGraphWalker(policy.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    Node visitedNode;
                    visitedNode = policy.graph().getNode(n);
                    if (visitedNode.getType().equals(PC)) {
                        attrs.add(n);
                    }
                })
                .walk(node);

        return attrs;
    }

    @Override
    public boolean isContained(String subject, String container) throws PMException {
        if (!policy.graph().nodeExists(subject)) {
            throw new NodeDoesNotExistException(subject);
        } else if (!policy.graph().nodeExists(container)){
            throw new NodeDoesNotExistException(container);
        }

        AtomicBoolean found = new AtomicBoolean(false);

        new DepthFirstGraphWalker(policy.graph())
                .withDirection(Direction.PARENTS)
                .withVisitor((n) -> {
                    if (n.equals(container)) {
                        found.set(true);
                    }
                })
                .walk(subject);

        return found.get();
    }

}
