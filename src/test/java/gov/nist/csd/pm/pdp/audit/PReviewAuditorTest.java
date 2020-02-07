package gov.nist.csd.pm.pdp.audit;

import gov.nist.csd.pm.operations.OperationSet;
import gov.nist.csd.pm.pdp.audit.model.Explain;
import gov.nist.csd.pm.pdp.audit.model.Path;
import gov.nist.csd.pm.pdp.audit.model.PolicyClass;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;
import static org.junit.jupiter.api.Assertions.*;

class PReviewAuditorTest {

    @Test
    void testExplain() throws PMException {
        for(TestCases.TestCase tc : TestCases.getTests()) {
            PReviewAuditor auditor = new PReviewAuditor(tc.graph);
            Explain explain = auditor.explain(TestCases.u1ID, TestCases.o1ID);
            System.out.println(explain);

            assertTrue(explain.getPermissions().containsAll(tc.getExpectedOps()),
                    tc.name + " expected ops " + tc.getExpectedOps() + " but got " + explain.getPermissions());

            for (String pcName : tc.expectedPaths.keySet()) {
                List<String> paths = tc.expectedPaths.get(pcName);
                assertNotNull(paths, tc.name);

                PolicyClass pc = explain.getPolicyClasses().get(pcName);
                for (String exPathStr : paths) {
                    boolean match = false;
                    for (Path resPath : pc.getPaths()) {
                        if(pathsMatch(exPathStr, resPath.toString())) {
                            match = true;
                            break;
                        }
                    }
                    assertTrue(match, tc.name + " expected path \"" + exPathStr + "\" but it was not in the results \"" + pc.getPaths() + "\"");
                }
            }
        }
    }

    private boolean pathsMatch(String expectedStr, String actualStr) {
        String[] expectedArr = expectedStr.split("-");
        String[] actualArr = actualStr.split("-");

        if (expectedArr.length != actualArr.length) {
            return false;
        }

        for (int i = 0; i < expectedArr.length; i++) {
            String ex = expectedArr[i];
            String res = actualArr[i];
            // if the element has brackets, it's a list of permissions
            if (ex.startsWith("[") && res.startsWith("[")) {
                // trim the brackets from the strings
                ex = ex.substring(1, ex.length()-1);
                res = res.substring(1, res.length()-1);

                // split both into an array of strings
                String[] exOps = ex.split(",");
                String[] resOps = res.split(",");

                Arrays.sort(exOps);
                Arrays.sort(resOps);

                if (exOps.length != resOps.length) {
                    return false;
                }
                for (int j = 0; j < exOps.length; j++) {
                    if (!exOps[j].equals(resOps[j])) {
                        return false;
                    }
                }
            } else if (!ex.equals(actualArr[i])) {
                return false;
            }
        }

        return true;
    }

    @Test
    void test1() throws PMException {
        Random random = new Random();
        Graph graph =new MemGraph();
        Node rbac = graph.createPolicyClass(random.nextLong(), "RBAC", null);
        Node records = graph.createNode(random.nextLong(), "records", OA, null, rbac.getID());

        graph.createNode(random.nextLong(), "recA1", OA, null, records.getID());
        graph.createNode(random.nextLong(), "recA2", OA, null, records.getID());
        graph.createNode(random.nextLong(), "recA3", OA, null, records.getID());
        graph.createNode(random.nextLong(), "recA4", OA, null, records.getID());

        Node nurse = graph.createNode(random.nextLong(), "Nurse", UA, null, rbac.getID());
        Node doctor = graph.createNode(random.nextLong(), "Doctor", UA, null, nurse.getID());

        Node u1 = graph.createNode(random.nextLong(), "u1", U, null, doctor.getID());

        graph.associate(nurse.getID(), records.getID(), new OperationSet("read"));
        graph.associate(doctor.getID(), records.getID(), new OperationSet("write"));

        System.out.println(new PReviewAuditor(graph).explain(u1.getID(), records.getID()));

    }

    @Test
    void test2() throws PMException {
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
                "node UA doctorA1\n" +
                "node UA nurseA1\n" +
                "node UA patient1\n" +
                "assign UA:users PC:DAC\n" +
                "assign UA:nurseA1 UA:users\n" +
                "assign UA:doctorA1 UA:users\n" +
                "assign UA:patient1 UA:users\n" +
                "assign U:nurseA1 UA:nurseA1\n" +
                "assign U:doctorA1 UA:doctorA1\n" +
                "assign U:patient1 UA:patient1\n" +
                "\n" +
                "node OA homes\n" +
                "node OA doctorA1_home\n" +
                "node OA nurseA1_home\n" +
                "node OA patient1_home\n" +
                "node OA dac_records\n" +
                "\n" +
                "assign OA:homes PC:DAC\n" +
                "assign OA:doctorA1_home OA:homes\n" +
                "assign OA:nurseA1_home OA:homes\n" +
                "assign OA:patient1_home OA:homes\n" +
                "assign OA:patient_1_records OA:doctorA1_home\n" +
                "assign OA:patient_1_records OA:nurseA1_home\n" +
                "assign OA:patient_1_records OA:patient1_home\n" +
                "\n" +
                "assoc UA:users OA:homes [assign to]\n" +
                "assoc UA:doctorA1 OA:doctorA1_home [read,write]\n" +
                "assoc UA:nurseA1 OA:nurseA1_home [read,write]\n" +
                "assoc UA:patient1 OA:patient1_home [read,write,assign]\n" +
                "\n" +
                "# LogIn\n" +
                "node PC LogIn\n" +
                "node UA okta_users\n" +
                "node UA local_users\n" +
                "assign UA:okta_users PC:LogIn\n" +
                "assign UA:local_users PC:LogIn\n" +
                "assign U:doctorA1 UA:local_users\n" +
                "assign U:nurseA1 UA:local_users\n" +
                "\n" +
                "\n" +
                "assign OA:records PC:LogIn\n" +
                "\n" +
                "assoc UA:okta_users OA:records [read]\n" +
                "assoc UA:local_users OA:records [read,write]";

        Graph graph = GraphSerializer.deserialize(new MemGraph(), s);

        Node recNode = graph.getNode("patient1_rec_1", OA, null);
        Node userNode = graph.getNode("doctorA1", U, null);

        Explain explain = new PReviewAuditor(graph).explain(userNode.getID(), recNode.getID());
        System.out.println(explain);

    }
}