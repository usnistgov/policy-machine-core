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

import static org.junit.jupiter.api.Assertions.*;

class GraphServiceTest {

    @Test
    void test() throws PMException {
        String s = "# RBAC\n" +
                "node PC RBAC\n" +
                "node OA records\n" +
                "\n" +
                "assign OA:records PC:RBAC\n" +
                "\n" +
                "node OA patient_1_records\n" +
                "node OA patient1_rec_1\n" +
                "node OA patient1_rec_2\n" +
                "node OA patient1_rec_3\n" +
                "node OA patient1_rec_4\n" +
                "\n" +
                "assign OA:patient_1_records OA:records\n" +
                "assign OA:patient1_rec_1 OA:patient_1_records\n" +
                "assign OA:patient1_rec_2 OA:patient_1_records\n" +
                "assign OA:patient1_rec_3 OA:patient_1_records\n" +
                "assign OA:patient1_rec_4 OA:patient_1_records\n" +
                "\n" +
                "node UA Doctor\n" +
                "node UA Nurse\n" +
                "node UA Patient\n" +
                "\n" +
                "node U doctorA1\n" +
                "node U nurseA1\n" +
                "node U patient1\n" +
                "\n" +
                "assign UA:Nurse PC:RBAC\n" +
                "assign UA:Doctor UA:Nurse\n" +
                "assign U:doctorA1 UA:Doctor\n" +
                "assign U:nurseA1 UA:Nurse\n" +
                "assign UA:Patient PC:RBAC\n" +
                "assign U:patient1 UA:Patient\n" +
                "\n" +
                "assoc UA:Nurse OA:records [read]\n" +
                "assoc UA:Doctor OA:records [write]\n" +
                "assoc UA:Patient OA:patient_1_records [read, assign]\n" +
                "\n" +
                "# DAC\n" +
                "node PC DAC\n" +
                "node UA users\n" +
                "node UA doctorA1_UA\n" +
                "node UA nurseA1_UA\n" +
                "node UA patient1_UA\n" +
                "assign UA:users PC:DAC\n" +
                "assign UA:nurseA1_UA UA:users\n" +
                "assign UA:doctorA1_UA UA:users\n" +
                "assign UA:patient1_UA UA:users\n" +
                "assign U:nurseA1 UA:nurseA1_UA\n" +
                "assign U:doctorA1 UA:doctorA1_UA\n" +
                "assign U:patient1 UA:patient1_UA\n" +
                "\n" +
                "node OA homes\n" +
                "node OA doctorA1_home\n" +
                "node OA nurseA1_home\n" +
                "node OA patient1_home\n" +
                "assign OA:homes PC:DAC\n" +
                "assign OA:doctorA1_home OA:homes\n" +
                "assign OA:nurseA1_home OA:homes\n" +
                "assign OA:patient1_home OA:homes\n" +
                "assign OA:records OA:doctorA1_home\n" +
                "assign OA:records OA:nurseA1_home\n" +
                "assign OA:records OA:patient1_home\n" +
                "\n" +
                "assoc UA:users OA:homes [assign to]\n" +
                "assoc UA:doctorA1_UA OA:doctorA1_home [read,write]\n" +
                "assoc UA:nurseA1_UA OA:nurseA1_home [read,write]\n" +
                "assoc UA:patient1_UA OA:patient1_home [read,write,assign]\n" +
                "\n" +
                "# LogIn\n" +
                "node PC LogIn\n" +
                "node UA okta_users\n" +
                "node UA local_users\n" +
                "assign UA:okta_users PC:LogIn\n" +
                "assign UA:local_users PC:LogIn\n" +
                "\n" +
                "assign OA:records PC:LogIn\n" +
                "\n" +
                "assoc UA:okta_users OA:records [read]\n" +
                "assoc UA:local_users OA:records [read,write]";

        PDP pdp = new PDP(new PAP(new MemGraph(), new MemProhibitions(), new MemObligations()), null);
        GraphSerializer.deserialize(pdp.getGraphService(new UserContext(0, 0)), s);

        Set<Node> papNodes = pdp.getPAP().getGraphPAP().getNodes();
        UserContext userContext = new UserContext(0, 0);
        Graph graphService = pdp.getGraphService(userContext);
        Set<Node> pdpNodes = graphService.getNodes();

        // -1 because the super user will have access to all nodes except the UA which gives it access to itself (superUA2)
        assertEquals(papNodes.size()-1, pdpNodes.size());
    }

}