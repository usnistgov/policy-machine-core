package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.audit.model.Explain;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.common.FunctionalEntity;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;

import java.util.*;

/**
 * Methods to analyze NGAC data.
 */
public class AnalyticsService extends Service {

    public AnalyticsService(UserContext userCtx, FunctionalEntity pap, EPP epp, Decider decider, Auditor auditor) {
        super(userCtx, pap, epp, decider, auditor);
    }

    /**
     * Given the name of a target node, return the permissions the current user has on it.
     * @param target the name of the target node.
     * @return the set of operations the current user has on the target node.
     * @throws PMException if there is an error getting the permissions for the current user on the target
     */
    public Set<String> getPermissions(String target) throws PMException {
        Decider decider = getDecider();
        return decider.list(userCtx.getUser(), userCtx.getProcess(), target);
    }


    /**
     * Get the Personal Object System for the user of the current session.  This method returns the first level of nodes
     * the user has direct access to.
     * @return the set of nodes that the user has direct access.
     */
    public Set<Node> getPos(UserContext userCtx) throws PMException {
        // Prepare the hashset to return.
        HashSet<Node> hsOa = new HashSet<>();

        // Call find_border_oa_priv(u). The result is a Hashtable
        // htoa = {oa -> {op -> pcset}}:
        Hashtable<String, Hashtable<String, Set<String>>> htOa = findBorderOaPrivRestrictedInternal(userCtx);

        // For each returned oa (key in htOa)
        for (Enumeration<String> oas = htOa.keys(); oas.hasMoreElements(); ) {
            String oa = oas.nextElement();

            // Compute oa's required PCs by calling find_pc_set(oa).
            HashSet<String> hsReqPcs = inMemFindPcSet(oa);
            // Extract oa's label.
            Hashtable<String, Set<String>> htOaLabel = htOa.get(oa);

            // Walk through the op -> pcset of the oa's label.
            // For each operation/access right
            for (Enumeration ops = htOaLabel.keys(); ops.hasMoreElements(); ) {
                String sOp = (String)ops.nextElement();
                // Extract the pcset corresponding to this operation/access right.
                Set<String> hsActualPcs = htOaLabel.get(sOp);
                // if the set of required PCs is a subset of the actual pcset,
                // then user u has some privileges on the current oa node.
                if (hsActualPcs.containsAll(hsReqPcs)) {
                    hsOa.add(getGraphAdmin().getNode(oa));
                    break;
                }
            }
        }

        return new HashSet<>(hsOa);
    }

    private Hashtable<String, Hashtable<String, Set<String>>> findBorderOaPrivRestrictedInternal(UserContext userCtx) throws PMException {
        // Uses a hashtable htReachableOas of reachable oas (see find_border_oa_priv(u))
        // An oa is a key in this hashtable. The value is another hashtable that
        // represents a label of the oa. A label is a set of pairs {(op -> pcset)}, with
        // the op being the key and pcset being the value.
        // {oa -> {op -> pcset}}.
        Hashtable<String, Hashtable<String, Set<String>>> htReachableOas = new Hashtable<>();

        // BFS from u (the base node). Prepare a queue.
        Set<String> visited = new HashSet<>();
        String crtNode;

        // Get u's directly assigned attributes and put them into the queue.
        Set<String> hsAttrs = getGraphAdmin().getParents(userCtx.getUser());
        List<String> queue = new ArrayList<>(hsAttrs);

        // While the queue has elements, extract an element from the queue
        // and visit it.
        while (!queue.isEmpty()) {
            // Extract an ua from queue.
            crtNode = queue.remove(0);
            if (!visited.contains(crtNode)) {
                // If the ua has ua -> oa edges
                if (inMemUattrHasOpsets(crtNode)) {
                    // Find the set of PCs reachable from ua.
                    HashSet<String> hsUaPcs = inMemFindPcSet(crtNode);

                    // From each discovered ua traverse the edges ua -> oa.

                    // Find the opsets of this user attribute. Note that the set of containers for this
                    // node (user attribute) may contain not only opsets.
                    Map<String, OperationSet> assocs = getGraphAdmin().getSourceAssociations(crtNode);

                    // Go through the containers and only for opsets do the following.
                    // For each opset ops of ua:
                    for (String target : assocs.keySet()) {
                        // If oa is in htReachableOas
                        if (htReachableOas.containsKey(target)) {
                            // Then oa has a label op1 -> hsPcs1, op2 -> hsPcs2,...
                            // Extract its label:
                            Hashtable<String, Set<String>> htOaLabel = htReachableOas.get(target);

                            // Get the operations from the opset:
                            Set opers = assocs.get(target);
                            // For each operation in the opset
                            for (Object oper : opers) {
                                String sOp = (String) oper;
                                // If the oa's label already contains the operation sOp
                                if (htOaLabel.containsKey(sOp)) {
                                    // The label contains op -> some pcset.
                                    // Do the union of the old pc with ua's pcset
                                    Set<String> hsPcs = htOaLabel.get(sOp);
                                    hsPcs.addAll(hsUaPcs);
                                } else {
                                    // The op is not in the oa's label.
                                    // Create new op -> ua's pcs mappiing in the label.
                                    Set<String> hsNewPcs = new HashSet<>(hsUaPcs);
                                    htOaLabel.put(sOp, hsNewPcs);
                                }
                            }
                        } else {
                            // oa is not in htReachableOas.
                            // Prepare a new label
                            Hashtable<String, Set<String>> htOaLabel = new Hashtable<>();

                            // Get the operations from the opset:
                            Set opers = assocs.get(target);
                            // For each operation in the opset
                            for (Object oper : opers) {
                                String sOp = (String) oper;
                                // Add op -> pcs to the label.
                                Set<String> hsNewPcs = new HashSet<>(hsUaPcs);
                                htOaLabel.put(sOp, hsNewPcs);
                            }

                            // Add oa -> {op -> pcs}
                            htReachableOas.put(target,  htOaLabel);
                        }
                    }
                }
                visited.add(crtNode);

                Set<String> hsDescs = getGraphAdmin().getParents(crtNode);
                queue.addAll(hsDescs);
            }
        }


        // For each reachable oa in htReachableOas.keys
        for (Enumeration<String> keys = htReachableOas.keys(); keys.hasMoreElements() ;) {
            String oa = keys.nextElement();
            // Compute {pc | oa ->+ pc}
            Set<String> hsOaPcs = inMemFindPcSet(oa);
            // Extract oa's label.
            Hashtable<String, Set<String>> htOaLabel = htReachableOas.get(oa);
            // The label contains op1 -> pcs1, op2 -> pcs2,...
            // For each operation in the label
            for (Enumeration<String> lbl = htOaLabel.keys(); lbl.hasMoreElements();) {
                String sOp = lbl.nextElement();
                // Intersect the pcset corresponding to this operation,
                // which comes from the uas, with the oa's pcset.
                Set<String> oaPcs = htOaLabel.get(sOp);
                oaPcs.retainAll(hsOaPcs);
                if (oaPcs.isEmpty()) htOaLabel.remove(sOp);
            }
        }

        return htReachableOas;
    }

    private HashSet<String> inMemFindPcSet(String node) throws PMException {
        HashSet<String> reachable = new HashSet<>();

        // Init the queue, visited
        ArrayList<String> queue = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        // The current element
        String crtNode;

        // Insert the start node into the queue
        queue.add(node);

        Set<String> policyClasses = getGraphAdmin().getPolicyClasses();

        // While queue is not empty
        while (!queue.isEmpty()) {
            // Extract current element from queue
            crtNode = queue.remove(0);
            // If not visited
            if (!visited.contains(crtNode)) {
                // Mark it as visited
                visited.add(crtNode);
                // Extract its direct descendants. If a descendant is an attribute,
                // insert it into the queue. If it is a pc, add it to reachable,
                // if not already there
                Set<String> hsContainers = getGraphAdmin().getParents(crtNode);
                for (String n : hsContainers) {
                    if (policyClasses.contains(n)) {
                        reachable.add(n);
                    } else {
                        queue.add(n);
                    }
                }
            }
        }
        return reachable;
    }

    private boolean inMemUattrHasOpsets(String uaNode) throws PMException {
        return !getGraphAdmin().getSourceAssociations(uaNode).isEmpty();
    }

    public Explain explain(String user, String target) throws PMException {
        return getAuditor().explain(user, target);
    }
}
