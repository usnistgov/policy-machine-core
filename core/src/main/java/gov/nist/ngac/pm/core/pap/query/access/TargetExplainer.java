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

package gov.nist.ngac.pm.core.pap.query.access;

import static gov.nist.ngac.pm.core.common.graph.node.NodeType.PC;
import static gov.nist.ngac.pm.core.pap.admin.AdminPolicyNode.PM_ADMIN_POLICY_CLASSES;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.graph.dag.GraphWalker;
import gov.nist.ngac.pm.core.pap.graph.dag.Propagator;
import gov.nist.ngac.pm.core.common.graph.node.Node;
import gov.nist.ngac.pm.core.pap.graph.Association;
import gov.nist.ngac.pm.core.pap.graph.dag.DepthFirstGraphWalker;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.explain.Path;
import gov.nist.ngac.pm.core.pap.store.PolicyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Finds every ascendant path from a target to each policy class it reaches, along with the associations
 * touching each node on the path.
 */
public class TargetExplainer {

	private final PolicyStore policyStore;

	public TargetExplainer(PolicyStore policyStore) {
		this.policyStore = policyStore;
	}

	/**
	 * Finds every ascendant path from the target to each policy class it reaches.
	 *
	 * @param targetCtx the target to explain
	 * @return a map from policy class node to its paths from the target, each path paired with the
	 * associations touching a node on it
	 * @throws PMException if the walk fails
	 */
	public Map<Node, Map<Path, List<Association>>> explainTarget(TargetContext targetCtx) throws PMException {
		Collection<Long> policyClasses = policyStore.graph().getPolicyClasses();

		// initialize map with policy classes
		Map<Node, Map<List<Node>, List<Association>>> pcPathAssociations = new HashMap<>();
		for (long pc : policyClasses) {
			Node pcNode = policyStore.graph().getNodeById(pc);
			pcPathAssociations.put(pcNode, new HashMap<>(Map.of(new ArrayList<>(List.of(pcNode)), new ArrayList<>())));
		}

		Propagator propagator = (src, dst) -> {
			Node srcNode = policyStore.graph().getNodeById(src);
			Node dstNode =  policyStore.graph().getNodeById(dst);

			Map<List<Node>, List<Association>> srcPathAssocs = pcPathAssociations.get(srcNode);
			Map<List<Node>, List<Association>> dstPathAssocs = pcPathAssociations.getOrDefault(dstNode, new HashMap<>());

			for (Map.Entry<List<Node>, List<Association>> entry : srcPathAssocs.entrySet()) {
				// add DST to the path from SRC
				List<Node> targetPath = new ArrayList<>(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());
				targetPath.addFirst(dstNode);

				// collect any associations for the DST node
				Association[] associationsWithTarget = policyStore.graph().getAssociationsWithTarget(dst).toArray(new Association[0]);
				associations.addAll(List.of(associationsWithTarget));
				dstPathAssocs.put(targetPath, associations);
			}

			// update dst entry
			pcPathAssociations.put(dstNode, dstPathAssocs);
		};

		// DFS from target node(s)
		GraphWalker dfs = new DepthFirstGraphWalker(policyStore.graph()::getAdjacentDescendants)
				.withPropagator(propagator);

		List<Node> nodes = new ArrayList<>();
		if (targetCtx instanceof NodeTargetContext) {
			long nodeId = targetCtx.resolveNodeIds(policyStore.graph()).iterator().next();
			Node targetNode = policyStore.graph().getNodeById(nodeId);
			long walkId = targetNode.getType().equals(PC) ? PM_ADMIN_POLICY_CLASSES.nodeId() : nodeId;
			nodes.add(targetNode);
			dfs.walk(walkId);
		} else {
			// AnonymousTargetContext
			for (long id : targetCtx.resolveNodeIds(policyStore.graph())) {
				nodes.add(policyStore.graph().getNodeById(id));
				dfs.walk(id);
			}
		}

		// convert the map created above into a map where the policy classes are the keys
		Map<Node, Map<Path, List<Association>>> pcMap = new HashMap<>();

		for (Node target : nodes) {
			Map<List<Node>, List<Association>> targetPathAssocs = pcPathAssociations.get(target);
			for (Map.Entry<List<Node>, List<Association>> entry : targetPathAssocs.entrySet()) {
				Path targetPath = new Path(entry.getKey());
				List<Association> associations = new ArrayList<>(entry.getValue());

				Node pc = targetPath.getLast();

				Map<Path, List<Association>> pcPathAssocs = pcMap.getOrDefault(pc, new HashMap<>());
				pcPathAssocs.put(targetPath, associations);
				pcMap.put(pc, pcPathAssocs);
			}
		}

		return pcMap;
	}

}
