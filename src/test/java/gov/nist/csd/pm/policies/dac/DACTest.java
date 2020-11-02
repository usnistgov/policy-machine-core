package gov.nist.csd.pm.policies.dac;

import gov.nist.csd.pm.epp.EPPOptions;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.operations.Operations;
import gov.nist.csd.pm.pap.GraphAdmin;
import gov.nist.csd.pm.pap.ObligationsAdmin;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.ProhibitionsAdmin;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.GraphService;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static gov.nist.csd.pm.pdp.PDP.newPDP;
import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.OA;
import static org.junit.jupiter.api.Assertions.*;

class DACTest {

    @Test
    void testConfigureAndDelegate() throws PMException {

        // Config Stuff
        PDP pdp = newPDP(
                new PAP(
                        new GraphAdmin(new MemGraph()),
                        new ProhibitionsAdmin(new MemProhibitions()),
                        new ObligationsAdmin(new MemObligations())
                ),
                new EPPOptions(),
                new OperationSet("read", "write", "execute")
        );

        Node superUser = pdp.getEPP().getPAP().getGraphAdmin().getSuperPolicy().getSuperUser();
        UserContext superContext = new UserContext(superUser.getName());

        Graph graph = pdp.getGraphService(superContext);


        // Test configure
        Node dac = DAC.configure(null, pdp, superContext);
        Node dac_users = DAC.DAC_USERS_NODE;
        Node dac_objects = DAC.DAC_OBJECTS_NODE;

        assertNotNull(dac_users, "DAC_Users was not configured.");
        assertNotNull(dac_objects, "DAC_Objects was not configured.");

        Node delegator = graph.createNode(
                "bob",
                NodeType.U,
                null,
                dac_users.getName()
        );

        Set<String> DAC_UA_children = graph.getChildren("DAC_default_UA");
        Set<String> DAC_OA_children = graph.getChildren("DAC_default_OA");

        assertTrue(
                DAC_UA_children.contains("bob_consent_admin") &&
                DAC_UA_children.contains("bob_consent_group") &&
                DAC_UA_children.contains("bob_consent_container_UA") &&
                DAC_OA_children.contains("bob_consent_container_OA"),
                "DAC consent obligation did not create corresponding consent nodes for bob"
        );

    }

    @Test
    void delegate() {
    }
}