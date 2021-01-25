package gov.nist.csd.pm.policies.dac.functionExecutors;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.AssignToEvent;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.epp.functions.FunctionExecutor;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.evr.EVRException;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import static gov.nist.csd.pm.operations.Operations.ALL_OPS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.UA;

public class ConfigConsentFunctionExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "config_consent";
    }

    @Override
    public int numParams() {
        return 0;
    }

    /**
     * Execute the function.
     *
     * @param graph             the graph
     * @param prohibitions      the prohibitions
     * @param obligations       the obligations
     * @param eventCtx          the event that is being processed
     * @param function          the function information
     * @param functionEvaluator a FunctionEvaluator to evaluate a nested functions
     * @return the object that the function is expected to return
     * @throws PMException if there is any error executing the function
     */
    public Object exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {

        if (!(eventCtx instanceof AssignToEvent)) {
            throw new EVRException("config_consent expected an AssignToEvent but got " + eventCtx.getClass());
        }
        AssignToEvent assignToEvent = (AssignToEvent)eventCtx;
        Node userNode = assignToEvent.getChildNode();


        //// create the consent configuration for this user -- ths user has already been assigned to dac_users
        // create a dac consent admin UA for this user, assign user to it, put it in DAC class
        Node userConsentAdminNode = graph.createNode(
                userNode.getName() + "_consent_admin",
                UA,
                Node.toProperties("consent_admin for", userNode.getName()),
                "DAC_default_UA"
        );
        graph.assign(userNode.getName(), userConsentAdminNode.getName());

        // create a dac consent group UA, associate consent admin to it, put it in DAC class
        Node userConsentGroup = graph.createNode(
                userNode.getName() + "_consent_group",
                UA,
                Node.toProperties("consent_group for", userNode.getName()),
                "DAC_default_UA"
        );
        graph.associate(userConsentAdminNode.getName(), userConsentGroup.getName(), new OperationSet(ALL_OPS));

        // create a dac consent container OA, associate consent admin to it, put it in DAC class
        Node userConsentContainerOA = graph.createNode(
                userNode.getName() + "_consent_container_OA",
                OA,
                Node.toProperties("consent_container_OA for", userNode.getName()),
                "DAC_default_OA"
        );
        graph.associate(userConsentAdminNode.getName(), userConsentContainerOA.getName(), new OperationSet(ALL_OPS));

        // create a dac consent container UA, associate consent admin to it, put it in DAC class
        Node userConsentContainerUA = graph.createNode(
                userNode.getName() + "_consent_container_UA",
                UA,
                Node.toProperties("consent_container_UA for", userNode.getName()),
                "DAC_default_UA"
        );
        graph.associate(userConsentAdminNode.getName(), userConsentContainerUA.getName(), new OperationSet(ALL_OPS));

        return null;
    }
}
