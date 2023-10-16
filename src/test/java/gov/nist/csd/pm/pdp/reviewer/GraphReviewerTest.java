package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphReviewerTest {

    private static GraphReviewer graphReviewer;

    @BeforeAll
    static void setup() throws PMException {
        String pml =
                "set resource access rights [\"read\", \"write\"]\n" +
                "create pc \"pc1\" {\n" +
                "    oas {\n" +
                "        \"oa1\"\n" +
                "            \"oa2\"\n" +
                "                \"oa3\"\n" +
                "        \"oa4\"\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "create pc \"pc2\" {\n" +
                "    oas {\n" +
                "        \"oa5\"\n" +
                "            \"oa6\"\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "create pc \"pc3\"\n" +
                "\n" +
                "create o \"o1\" assign to [\"oa3\", \"oa6\"]";
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        graphReviewer = new GraphReviewer(pap);
    }

    @Test
    void testGetAttributeContainers() throws PMException {
        List<String> conts = graphReviewer.getAttributeContainers("o1");
        List<String> expected = List.of("oa3", "oa2", "oa1", "oa6", "oa5");
        assertTrue(conts.containsAll(expected));
        assertTrue(expected.containsAll(conts));
    }

    @Test
    void testGetPolicyClassContainers() throws PMException {
        List<String> pcs = graphReviewer.getPolicyClassContainers("o1");
        List<String> expected = List.of("pc1", "pc2");
        assertTrue(pcs.containsAll(expected));
        assertTrue(expected.containsAll(pcs));
    }

    @Test
    void testIsContained() throws PMException {
        assertTrue(graphReviewer.isContained("o1", "oa1"));
        assertTrue(graphReviewer.isContained("o1", "oa2"));
        assertTrue(graphReviewer.isContained("o1", "oa3"));
        assertTrue(graphReviewer.isContained("o1", "pc1"));
        assertTrue(graphReviewer.isContained("o1", "pc2"));
        assertFalse(graphReviewer.isContained("o1", "pc3"));
    }

}