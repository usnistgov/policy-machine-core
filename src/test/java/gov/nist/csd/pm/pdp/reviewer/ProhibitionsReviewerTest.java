package gov.nist.csd.pm.pdp.reviewer;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.prohibition.Prohibition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProhibitionsReviewerTest {

    private static ProhibitionsReviewer prohibitionsReviewer;

    @BeforeAll
    static void setup() throws PMException {
        String pml = """
                set resource access rights ["read"]
                create pc "pc1" {
                    uas {
                        "ua1"
                            "ua2"
                                "ua3"
                    }
                    oas {
                        "oa1"
                        "oa2"
                    }
                }
                
                create u "u1" assign to ["ua3"]
                
                create prohibition "p1"
                deny UA "ua1"
                access rights ["read"]
                on intersection of ["oa1", "oa2"]
                
                create prohibition "p2"
                deny U "u1"
                access rights ["read"]
                on intersection of [!"oa1", "oa2"]
                """;
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        prohibitionsReviewer = new ProhibitionsReviewer(pap);
    }

    @Test
    void testGetInheritedProhibitionsFor() throws PMException {
        List<Prohibition> prohibitions = prohibitionsReviewer.getInheritedProhibitionsFor("u1");
        assertEquals(2, prohibitions.size());
    }

    @Test
    void testGetProhibitionsWithContainer() throws PMException {
        List<Prohibition> prohibitions = prohibitionsReviewer.getProhibitionsWithContainer("oa1");
        assertEquals(2, prohibitions.size());
    }

}