package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.epp.EPP;
import gov.nist.csd.pm.exceptions.PMException;
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

import static gov.nist.csd.pm.pdp.decider.PReviewDecider.ANY_OPERATIONS;
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

    /**
     * Create a new Service with a sessionID and processID from the request context.
     * @param pap the Policy Administration Point
     * @param epp the Event Processing Point
     */
    Service(PAP pap, EPP epp) {
        this.pap = pap;
        this.epp = epp;
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

    public Decider getDecider() throws PMException {
        return new PReviewDecider(getGraphPAP(), getProhibitionsPAP());
    }

    boolean hasPermissions(UserContext userCtx, String target, String... permissions) throws PMException {
        Decider decider = new PReviewDecider(pap.getGraphPAP(), pap.getProhibitionsPAP());

        Node node = pap.getGraphPAP().getNode(target);
        if (node.getType().equals(PC)) {
            if (!node.getProperties().containsKey(REP_PROPERTY)) {
                throw new PMException("unable to check permissions for policy class " + node.getName() + ", rep property not set");
            }

            target = node.getProperties().get(REP_PROPERTY);
        }

        Set<String> perms = decider.list(userCtx.getUser(), userCtx.getProcess(), target);
        if(permissions.length == 0 || Arrays.asList(permissions).contains(ANY_OPERATIONS)) {
            return !perms.isEmpty();
        } else if (perms.contains("*")) {
            return true;
        } else {
            return !perms.isEmpty() && perms.containsAll(Arrays.asList(permissions));
        }
    }
}
