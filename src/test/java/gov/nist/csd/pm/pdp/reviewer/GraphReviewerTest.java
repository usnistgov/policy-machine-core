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
        String pml = """
                set resource access rights ["read", "write"]
                create pc "pc1" {
                    oas {
                        "oa1"
                            "oa2"
                                "oa3"
                        "oa4"                                  
                    }
                }
                
                create pc "pc2" {
                    oas {
                        "oa5"
                            "oa6"
                    }
                }
                
                create pc "pc3"
                
                create o "o1" assign to ["oa3", "oa6"]
                """;
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