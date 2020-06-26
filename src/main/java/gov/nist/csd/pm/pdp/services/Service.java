package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pdp.policy.SuperPolicy;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

import java.util.Arrays;
import java.util.Set;

import static gov.nist.csd.pm.operations.Operations.ANY_OPERATIONS;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.PC;
import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;

/**
 * Class to provide common methods to all services.
 */
public class Service {

    private PAP pap;
    private EPP epp;
    SuperPolicy superPolicy;
    UserContext userCtx;
    private OperationSet resourceOps;

    /**
     * Create a new Service with a sessionID and processID from the request context.
     * @param pap the Policy Administration Point
     * @param epp the Event Processing Point
     */
    Service(PAP pap, EPP epp, OperationSet resourceOps) {
        this.pap = pap;
        this.epp = epp;
        this.resourceOps = resourceOps;
    }

    private Service() {}

    public void setUserCtx(UserContext userCtx) {
        this.userCtx = userCtx;
    }

    public UserContext getUserCtx() {
        return userCtx;
    }

    EPP getEPP() {
        return this.epp;
    }

    PAP getPAP() {
        return this.pap;
    }

    Graph getGraphPAP() {
        return pap.getGraphPAP();
    }

    Prohibitions getProhibitionsPAP() {
        return pap.getProhibitionsPAP();
    }

    Obligations getObligationsPAP() {
        return pap.getObligationsPAP();
    }

    public OperationSet getResourceOps() {
        return resourceOps;
    }

    public Decider getDecider() {
        return new PReviewDecider(getGraphPAP(), getProhibitionsPAP(), resourceOps);
    }

    boolean hasPermissions(UserContext userCtx, String target, String... permissions) throws PMException {
        Decider decider = new PReviewDecider(pap.getGraphPAP(), pap.getProhibitionsPAP(), resourceOps);

        Node node = pap.getGraphPAP().getNode(target);
        if (node.getType().equals(PC)) {
            if (!node.getProperties().containsKey(REP_PROPERTY)) {
                throw new PMException("unable to check permissions for policy class " + node.getName() + ", rep property not set");
            }

            target = node.getProperties().get(REP_PROPERTY);
        }

        Set<String> allowed = decider.list(userCtx.getUser(), userCtx.getProcess(), target);
        if(permissions.length == 0) {
            return !allowed.isEmpty();
        } else {
            return  allowed.containsAll(Arrays.asList(permissions));
        }
    }
}
