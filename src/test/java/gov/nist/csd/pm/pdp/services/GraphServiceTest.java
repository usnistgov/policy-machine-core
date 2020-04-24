package gov.nist.csd.pm.pdp.services;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.obligations.MemObligations;
import gov.nist.csd.pm.pip.prohibitions.MemProhibitions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.Properties.REP_PROPERTY;
import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    @Test
    void testPolicyClassReps() throws PMException {
        PDP pdp = new PDP(new PAP(new MemGraph(), new MemProhibitions(), new MemObligations()), null);
        Graph graph = pdp.getGraphService(new UserContext("super", ""));

        Node test = graph.createPolicyClass("test", null);
        String defUA = test.getProperties().get("default_ua");
        String defOA = test.getProperties().get("default_oa");
        String repProp = test.getProperties().get(REP_PROPERTY);

        assertTrue(graph.exists(defUA));
        assertTrue(graph.exists(defOA));
        assertTrue(graph.exists(repProp));
    }

    @Test
    void test() throws PMException {
        String s = "# RBAC\n" +
                "node PC RBAC\n" +
                "node OA records\n" +
                "\n" +
                "assign records RBAC\n" +
                "\n" +
                "node OA patient_1_records\n" +
                "node OA patient1_rec_1\n" +
                "node OA patient1_rec_2\n" +
                "node OA patient1_rec_3\n" +
                "node OA patient1_rec_4\n" +
                "\n" +
                "assign patient_1_records records\n" +
                "assign patient1_rec_1 patient_1_records\n" +
                "assign patient1_rec_2 patient_1_records\n" +
                "assign patient1_rec_3 patient_1_records\n" +
                "assign patient1_rec_4 patient_1_records\n" +
                "\n" +
                "node UA Doctor\n" +
                "node UA Nurse\n" +
                "node UA Patient\n" +
                "\n" +
                "node U doctorA1\n" +
                "node U nurseA1\n" +
                "node U patient1\n" +
                "\n" +
                "assign Nurse RBAC\n" +
                "assign Doctor Nurse\n" +
                "assign doctorA1 Doctor\n" +
                "assign nurseA1 Nurse\n" +
                "assign Patient RBAC\n" +
                "assign patient1 Patient\n" +
                "\n" +
                "assoc Nurse records [read]\n" +
                "assoc Doctor records [write]\n" +
                "assoc Patient patient_1_records [read, assign]\n" +
                "\n" +
                "# DAC\n" +
                "node PC DAC\n" +
                "node UA users\n" +
                "node UA doctorA1_UA\n" +
                "node UA nurseA1_UA\n" +
                "node UA patient1_UA\n" +
                "assign users DAC\n" +
                "assign nurseA1_UA users\n" +
                "assign doctorA1_UA users\n" +
                "assign patient1_UA users\n" +
                "assign nurseA1 nurseA1_UA\n" +
                "assign doctorA1 doctorA1_UA\n" +
                "assign patient1 patient1_UA\n" +
                "\n" +
                "node OA homes\n" +
                "node OA doctorA1_home\n" +
                "node OA nurseA1_home\n" +
                "node OA patient1_home\n" +
                "assign homes DAC\n" +
                "assign doctorA1_home homes\n" +
                "assign nurseA1_home homes\n" +
                "assign patient1_home homes\n" +
                "assign records doctorA1_home\n" +
                "assign records nurseA1_home\n" +
                "assign records patient1_home\n" +
                "\n" +
                "assoc users homes [assign to]\n" +
                "assoc doctorA1_UA doctorA1_home [read,write]\n" +
                "assoc nurseA1_UA nurseA1_home [read,write]\n" +
                "assoc patient1_UA patient1_home [read,write,assign]\n" +
                "\n" +
                "# LogIn\n" +
                "node PC LogIn\n" +
                "node UA okta_users\n" +
                "node UA local_users\n" +
                "assign okta_users LogIn\n" +
                "assign local_users LogIn\n" +
                "\n" +
                "assign records LogIn\n" +
                "\n" +
                "assoc okta_users records [read]\n" +
                "assoc local_users records [read,write]";

        PDP pdp = new PDP(new PAP(new MemGraph(), new MemProhibitions(), new MemObligations()), null);
        GraphSerializer.deserialize(pdp.getGraphService(new UserContext("super", "")), s);

        Set<Node> papNodes = pdp.getGraphService(new UserContext("super", "")).getNodes();
        UserContext userContext = new UserContext("super", "");
        Graph graphService = pdp.getGraphService(userContext);
        Set<Node> pdpNodes = graphService.getNodes();

        assertEquals(papNodes.size(), pdpNodes.size());
    }

}