package gov.nist.csd.pm.policies.dac;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Utilities for any operation relating to the DAC NGAC concept
 */
public class DAC {
    /**
     * This sets the DAC PC for any of the methods in this class.
     * If the given PC already exists it will mark it as the DAC PC,
     * otherwise it will create and mark it.
     *
     * This will likely be the first call in any method of this class.
     *
     * @param DACname the name of the DAC PC
     * @param graph
     * @return the DAC PC
     * @throws PMException
     */
    private static Node configure (String DACname, Graph graph) throws PMException {
        if (!graph.exists(DACname)) {
            return graph.createPolicyClass(DACname, Node.toProperties("ngac_type", "DAC"));
        } else {
            Node DAC = graph.getNode(DACname);

            // add ngac_type=DAC to properties
            String typeValue = DAC.getProperties().get("ngac_type");
            if (typeValue == null) {
                DAC.addProperty("ngac_type", "DAC");
            } else if (!typeValue.equals("DAC")) {
                throw new PMException("Node cannot have property key of ngac_type");
            }

            return DAC;
        }
    }

    /**
     * Add a delegation. This essentially creates an association from the context of the delegator.
     * There are a few necessary preconditions for this to run without exceptions:
     *  - All of the target types must be consistent (ether all OA/O or all UA/A)
     *  - Delegatee must either be a UA or U
     *  - Delegator must have "create association" access right on delegatee
     *  - Delegator must have "create association" and all given ops on all targets
     *
     * @param DACname the String of the DAC PC
     * @param graph
     * @param delegator the UserContext of the delegator
     * @param delegateeName the source of the delegation
     * @param ops the set of operations attempting to be delegated
     * @param targetNames the names of the targets of the delegation
     * @throws PMException if any of the above pre-conditions are not met
     */
    private static void delegate (String DACname, Graph graph, UserContext delegator,
                                  String delegateeName, OperationSet ops, Set<String> targetNames) throws PMException {

        Node delegatee = graph.getNode(delegateeName);
        Set<Node> targets = new HashSet<>();
        NodeType targetsType = null;
        Map<String, OperationSet> sourceAssociations = graph.getSourceAssociations(delegator.getUser());

        assert ops == null || ops.isEmpty() : "Operations cannot be null or empty";
        assert delegateeName == null : "Delegatee must exist in graph";
        assert graph == null : "Graph must exist";
        assert delegator == null : "Delegator must exist";


        // make sure that the DAC PC exists
        Node DAC = configure(DACname, graph);

        // check if delegatee is UA or U
        if (!(delegatee.getType() == NodeType.UA || delegatee.getType() == NodeType.U))
            throw new PMException("Delegatee must either be a UA or U");

        // get all targets and check if all the targets are a consistent type AND get also get that type
        for (String targetName: targetNames) {
            Node _target = graph.getNode(targetName);
            if (targetsType == null) { //
                if (_target.getType() == NodeType.UA || _target.getType() == NodeType.U)
                    targetsType = NodeType.UA;
                else if (_target.getType() == NodeType.OA ||_target.getType() == NodeType.O)
                    targetsType = NodeType.OA;
            } else {
                if (targetsType == NodeType.UA) {
                    if (!(_target.getType() == NodeType.UA || _target.getType() == NodeType.U))
                        throw new PMException("Targets have to have consistent types.");
                } else if (targetsType == NodeType.OA) {
                    if (!(_target.getType() == NodeType.OA || _target.getType() == NodeType.O))
                        throw new PMException("Targets have to have consistent types.");
                }
            }

            if (_target.getType() == NodeType.PC) {
                throw new PMException("Targets cannot be a PC.");
            }

            targets.add(_target);
        }

        // check if delegator has create association on the delegatee
        OperationSet delegateeOps = sourceAssociations.get(delegatee);
        if (!delegateeOps.contains(Operations.CREATE_ASSOCIATION)) {
            throw new PMException("Delegator must have the 'Create Association' Access Right on delegatee.");
        }

        // check if delegator has all ops adn create association on ALL of the targets
        for (String targetName: targetNames) {
            OperationSet _targetOps = sourceAssociations.get(targetName);

            if (!_targetOps.containsAll(ops)) {
                throw new PMException("Delegator must have all of the given Access Rights on all of the targets.");
            }

            if (!_targetOps.contains(Operations.CREATE_ASSOCIATION)) {
                throw new PMException("Delegator must have the 'Create Association' Access Right on all of the targets.");
            }
        }


        /////// EVERYTHING checked at this point, and the delegation can be created
        // create user attribute over delegatee (delegator_delegatee_delegation_UUID)
        String delegationID = UUID.randomUUID().toString();
        Node delegationUA = graph.createNode(
                delegator.getUser() + "_" + delegateeName + "_delegationUA_" + delegationID,
                NodeType.UA,
                Node.toProperties("delegationID", delegationID),
                DAC.getName()
        );
        graph.assign(delegateeName, delegationUA.getName());

        // create respective attribute over target (delegator_delegatee_delegation_UUID)
        Node delegationTargetA = graph.createNode(
                delegator.getUser() + "_" + delegateeName + "_delegationOA_" + delegationID,
                targetsType,
                Node.toProperties("delegationID", delegationID),
                DAC.getName()
        );
        for (String targetName: targetNames) {
            graph.assign(targetName, delegationTargetA.getName());
        }

        // associate the attributes with the give permissions
        graph.associate(delegationUA.getName(), delegationTargetA.getName(), ops);

    }

    // todo: add prohibitions
//    private static void delegate (String DACname, Graph graph, UserContext delegator,
//                                  String delegatee, OperationSet permission, Set<String> target) throws PMException {
//        configure(DACname, graph);
//        // check if delegator
//    }
}
