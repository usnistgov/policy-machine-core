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
}