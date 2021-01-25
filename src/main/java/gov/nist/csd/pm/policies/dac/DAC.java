package gov.nist.csd.pm.policies.dac;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.AnalyticsService;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.evr.EVRParser;
import gov.nist.csd.pm.pip.obligations.model.Obligation;
import gov.nist.csd.pm.policies.dac.functionExecutors.ConfigConsentFunctionExecutor;

import java.util.*;

/**
 * Utilities for any operation relating to the DAC NGAC concept
 *
 * Assumptions:
 *  -
 */
public class DAC {

    public static String DAC_USERS_NAME = "DAC_users";
    public static String DAC_OBJECTS_NAME = "DAC_objects";
    public static String DAC_PC_NAME = "DAC";
    public static Node DAC_USERS_NODE = null;
    public static Node DAC_OBJECTS_NODE = null;
    public static Node DAC_PC_NODE = null;


    /**
     * This sets/makes the DAC PC for any of the methods in this class.
     * If the given PC already exists it will mark it as the DAC PC,
     * otherwise it will create and mark it.
     *
     * This method will add DAC Attributes:
     *  - DAC_Users: contains all of the users in the graph
     *  - DAC_Objects: contains all of the user_home OAs
     *
     * This method will also added the consent obligation.
     * This will automatically the user delegation configuration whenever a user is added to the DAC.
     * The obligation will, for any user added to the DAC_users UA,:
     *  - create:
     *    - user_consent_admin
     *    - user_consent_group
     *    - user_consent_container_UA
     *    - user_consent_container_OA
     *  - associate:
     *    - consent_admin to group
     *    - consent_admin to container_UA
     *    - consent_admin to container_OA
     *  - assign:
     *    - user to user admin
     *
     *
     * This will likely be the first call in any method of this class.
     *
     *
     * @param DACname the name of the DAC PC, null if you want to use the default name
     * @param pdp PDP of the existing graph
     * @param superUserContext UserContext of the super user
     * @return the DAC PC
     * @throws PMException if anything goes wrong
     */
    public static Node configure (String DACname, PDP pdp, UserContext superUserContext) throws PMException {

        Graph graph = pdp.withUser(superUserContext).getGraph();
        Obligations obligations = pdp.withUser(new UserContext("super")).getObligations();

        // todo: on boarding methods

        //// Creating and Adding the DAC PC and Attributes
        if (DACname != null) {
            DAC_PC_NAME = DACname;
        }

        // DAC PC
        DAC_PC_NODE = checkAndCreateDACNode(graph, DAC_PC_NAME, NodeType.PC);

        // DAC_users UA
        DAC_USERS_NODE = checkAndCreateDACNode(graph, DAC_USERS_NAME, NodeType.UA);

        // DAC_objects OA
        DAC_OBJECTS_NODE = checkAndCreateDACNode(graph, DAC_OBJECTS_NAME, NodeType.OA);


        //// Adding the consent obligation for user config
        // first add the function executor
        pdp.getEPP().addFunctionExecutor(new ConfigConsentFunctionExecutor());
        obligations.add(getConsentObligation(superUserContext), true);


        // return the DAC PC
        return DAC_PC_NODE;
    }



    /**
     * Add a delegation. This essentially creates an association from the context of the delegator.
     * There are a few necessary preconditions for this to run without exceptions:
     *  - All of the target types must be consistent (ether all OA/O or all UA/A)
     *  - Delegatee must either be a UA or U
     *  - Delegator must have "create association" and "assign" access rights on delegatee
     *  - Delegator must have "create association", "assign" and all given ops on all targets
     *
     * @param pdp the PDP for the graph
     * @param delegator the UserContext of the delegator
     * @param delegateeName the source of the delegation
     * @param ops the set of operations attempting to be delegated
     * @param targetNames the names of the targets of the delegation
     * @throws PMException if any of the above pre-conditions are not met
     */
    public static void delegate (PDP pdp, UserContext delegator,
                                 String delegateeName, OperationSet ops, Set<String> targetNames) throws PMException {
        assert !(ops == null || ops.isEmpty()) : "Operations cannot be null or empty";
        assert delegateeName != null : "Delegatee must exist in graph";
        assert pdp != null : "PDP must exist";
        assert delegator != null : "Delegator must exist";
        assert !(targetNames == null || targetNames.isEmpty()) : "Must have targets";

        // todo: throw error if not configured

        Graph graph = pdp.withUser(delegator).getGraph();

        Node delegatee = graph.getNode(delegateeName);
        NodeType targetsType = null;
//        Map<String, OperationSet> sourceAssociations = graph.getSourceAssociations(delegator.getUser());
        AnalyticsService analyticsService = pdp.withUser(delegator).getAnalyticsService(delegator);

        //// Checking pre-conditions
        // check if delegatee is UA or U
        if (!(delegatee.getType() == NodeType.UA || delegatee.getType() == NodeType.U))
            throw new PMException("Delegatee must either be a UA or U");

        // get all targets and check if all the targets are a consistent type AND get also get that type
        for (String targetName: targetNames) {
            Node _target = graph.getNode(targetName);
            if (targetsType == null) {
                if (_target.getType() == NodeType.UA || _target.getType() == NodeType.U)
                    targetsType = NodeType.UA;
                else if (_target.getType() == NodeType.OA ||_target.getType() == NodeType.O)
                    targetsType = NodeType.OA;
            } else {
                if (targetsType == NodeType.UA) {
                    if (!(_target.getType() == NodeType.UA || _target.getType() == NodeType.U))
                        throw new PMException("Targets have to have consistent types.");
                } else {
                    if (!(_target.getType() == NodeType.OA || _target.getType() == NodeType.O))
                        throw new PMException("Targets have to have consistent types.");
                }
            }

            if (_target.getType() == NodeType.PC) {
                throw new PMException("Targets cannot be a PC.");
            }
        }

        // check if delegator has create association on the delegatee
        OperationSet delegateeOps = new OperationSet(analyticsService.getPermissions(delegatee.getName()));
//        OperationSet delegateeOps = sourceAssociations.get(delegatee.getName());
        if (!delegateeOps.contains(Operations.CREATE_ASSOCIATION)) {
            throw new PMException("Delegator must have the 'Create Association' Access Right on delegatee.");
        }

        if (!delegateeOps.contains(Operations.ASSIGN)) {
            throw new PMException("Delegator must have the 'Assign' Access Right on delegatee.");
        }

        // check if delegator has all ops adn create association on ALL of the targets
        for (String targetName: targetNames) {
//            OperationSet _targetOps = sourceAssociations.get(targetName);
            OperationSet _targetOps = new OperationSet (analyticsService.getPermissions(targetName));


            if (!_targetOps.containsAll(ops)) {
                throw new PMException("Delegator must have all of the given Access Rights on all of the targets.");
            }

            if (!_targetOps.contains(Operations.CREATE_ASSOCIATION)) {
                throw new PMException("Delegator must have the 'Create Association' Access Right on all of the targets.");
            }

            if (!_targetOps.contains(Operations.ASSIGN)) {
                throw new PMException("Delegator must have the 'Assign' Access Right on all of the targets.");
            }
        }


        //// Pre-conditions checked at this point, and the delegation can be created
        ConsentNodes consentNodes = findConsentNodes(graph, delegator.getUser());
        if (!consentNodes.consent_found()) {
            throw new PMException("Consent Nodes for user, " + delegator.getUser() + ", not properly created: ");
        }

        // create user attribute over delegatee (delegator_delegatee_delegation_UUID)
        String delegationID = UUID.randomUUID().toString();
        Node delegationUA = graph.createNode(
                delegator.getUser() + "_" + delegateeName + "_delegationUA_" + delegationID,
                NodeType.UA,
                Node.toProperties("delegationID", delegationID),
                consentNodes.consent_group.getName()
        );
        graph.assign(delegateeName, delegationUA.getName());


        // create respective attribute over target (delegator_delegatee_delegation_UUID)
        Node delegationTargetA;
        if (targetsType.equals(NodeType.OA)) {
            delegationTargetA = graph.createNode(
                    delegator.getUser() + "_" + delegateeName + "_delegationOA_" + delegationID,
                    targetsType,
                    Node.toProperties("delegationID", delegationID),
                    consentNodes.consent_container_oa.getName()
            );
        } else {
            delegationTargetA = graph.createNode(
                    delegator.getUser() + "_" + delegateeName + "_delegationUA_" + delegationID,
                    targetsType,
                    Node.toProperties("delegationID", delegationID),
                    // container ua
                    consentNodes.consent_container_ua.getName()
            );
        }

        for (String targetName: targetNames) {
            graph.assign(targetName, delegationTargetA.getName());
        }

        // associate the attributes with the give permissions
        graph.associate(delegationUA.getName(), delegationTargetA.getName(), ops);

    }

    // todo: add an over-ridden version of delegate which also takes into account prohibitions

    public static void assignOwner (PDP pdp, UserContext superUser, String ownerName,
                                    String... targetNames) throws PMException {
        // super graph
        Graph graph = pdp.withUser(superUser).getGraph();

        // find owner's consent ua and consent oa
        ConsentNodes consentNodesForOwner = findConsentNodes(graph, ownerName);
        if (!consentNodesForOwner.all_found()) {
            throw new PMException("Consent Nodes for user, " + ownerName + ", not properly created");
        }
        Node consent_ua = consentNodesForOwner.consent_container_ua;
        Node consent_oa = consentNodesForOwner.consent_container_oa;

        // assign each target to respective consent attr based on type
        for (String target : targetNames) {
            NodeType targetType = graph.getNode(target).getType();
            switch (targetType) {
                case O:
                    graph.assign(target, consent_oa.getName());
                    break;
                case U:
                    graph.assign(target, consent_ua.getName());
                    break;
                case OA:
                case UA:
                case PC:
                    throw new PMException("Node must be of type O or U");
            }
        }
    }

    public static Set<String> getAssignees (PDP pdp, UserContext superUser, String ownerName) throws PMException {
        // super graph
        Graph graph = pdp.withUser(superUser).getGraph();

        // get consent nodes for user
        ConsentNodes ownerConsentNodes = findConsentNodes(graph, ownerName);

        // get children of consent UA and consent OA (essentially get whats in their home folder.)
        Set<String> assignedUsers = graph.getChildren(ownerConsentNodes.consent_container_ua.getName());
        Set<String> assignedObjects = graph.getChildren(ownerConsentNodes.consent_container_oa.getName());

        // return combination of the two
        assignedUsers.addAll(assignedObjects);
        return assignedUsers;
    }

    /********************
     * Helper Functions *
     ********************/

    /**
     * Helper method to get the consent obligation.
     * This obligation will run the Consent Function Executor whenever a user is assigned to DAC_users.
     */
    private static Obligation getConsentObligation(UserContext userCtx) throws EVRException {
        String yaml =
                "label: consent obligation\n" +
                        "rules:\n" +
                        "  - label: consent rule\n" +
                        "    event:\n" +
                        "      subject:\n" +
                        "      operations:\n" +
                        "        - assign to\n" +
                        "      target:\n" +
                        "        policyElements:\n" +
                        "          - name: " + DAC_USERS_NAME + "\n" +
                        "            type: UA\n" +
                        "    response:\n" +
                        "      actions:\n" +
                        "        - function:\n" +
                        "            name: config_consent";
        EVRParser parser = new EVRParser();
        return parser.parse(userCtx.getUser(), yaml);
    }

    /**
     * Helper Method to check if a DAC node exists, and other wise create it.
     * It will also set the corresponding property for that DAC node.
     *
     * This methods is specifically for DAC nodes, and not meant to be used elsewhere
     */
    private static Node checkAndCreateDACNode(Graph graph, String name, NodeType type) throws PMException {
        Node DAC;
        if (!graph.exists(name)) {
            if (type == NodeType.PC) {
                return graph.createPolicyClass(name, Node.toProperties("ngac_type", "DAC"));
            } else {
                return graph.createNode (
                        name,
                        type,
                        Node.toProperties("ngac_type", "DAC"),
                        DAC_PC_NAME
                );
            }
        } else {
            DAC = graph.getNode(name);

            // add ngac_type=DAC to properties
            String typeValue = DAC.getProperties().get("ngac_type");
            if (typeValue == null) {
                DAC.addProperty("ngac_type", "DAC");
            } else if (!typeValue.equals("DAC")) {
                throw new PMException("Node cannot have property key of ngac_type");
            }
        }
        return DAC;
    }

    /**
     * Helper Method to find the corresponding consent nodes for a certain user.
     *
     * Under it a small class to group all of the nodes into one object
     */
    private static ConsentNodes findConsentNodes(Graph graph, String forUser) throws PMException {
        // todo: find nodes using properties or name?
        ConsentNodes consentNodes = new ConsentNodes();

        Set<Node> all_nodes = graph.getNodes();
        all_nodes.stream().forEach((n) -> {
            if (!consentNodes.all_found()) {
                String consentAdminFor = n.getProperties().get("consent_admin for");
                String consentGroupFor = n.getProperties().get("consent_group for");
                String consentContainerOAFor = n.getProperties().get("consent_container_OA for");
                String consentContainerUAFor = n.getProperties().get("consent_container_UA for");
                if (consentAdminFor != null && consentAdminFor.equals(forUser)) {
                    consentNodes.consent_admin = n;
                } else if (consentGroupFor != null && consentGroupFor.equals(forUser)) {
                    consentNodes.consent_group = n;
                } else if (consentContainerOAFor != null && consentContainerOAFor.equals(forUser)) {
                    consentNodes.consent_container_oa = n;
                } else if (consentContainerUAFor != null && consentContainerUAFor.equals(forUser)) {
                    consentNodes.consent_container_ua = n;
                }
            }
        });



        return consentNodes;
    }
    protected static class ConsentNodes {
        Node consent_admin = null;
        Node consent_group = null;
        Node consent_container_oa = null;
        Node consent_container_ua = null;

        boolean all_found () {
            return consent_admin != null &&
                    consent_group != null &&
                    consent_container_oa != null &&
                    consent_container_ua != null;
        }

        boolean consent_found () {
            return consent_group != null &&
                    consent_container_oa != null &&
                    consent_container_ua != null;
        }

        @Override
        public String toString() {
            return "ConsentNodes{" +
                    "consent_admin=" + consent_admin +
                    ", consent_group=" + consent_group +
                    ", consent_container_oa=" + consent_container_oa +
                    ", consent_container_ua=" + consent_container_ua +
                    '}';
        }
    }
}
