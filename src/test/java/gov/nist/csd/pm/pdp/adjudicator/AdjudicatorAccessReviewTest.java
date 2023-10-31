package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjudicatorAccessReviewTest {

    static AdjudicatorAccessReview u1;
    static AdjudicatorAccessReview u2;

    @BeforeAll
    static void setup() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(
                new UserContext("u1"),
                """
                set resource access rights ["read", "write"]
                
                create policy class "pc1" {
                    uas {
                        "ua1"
                        "ua2"
                    }
                    oas {
                        "oa1"
                            "oa1-1"
                            "oa1-2"
                    }
                    associations {
                        "ua1" and "ua2" with [review_policy]
                        "ua1" and "oa1" with [review_policy]
                    }
                }
                
                create user "u1" assign to ["ua1", "ua2"]
                create user "u2" assign to ["ua2"]    
                
                create o "o1" assign to ["oa1-1", "oa1-2"]            
                """,
                new PMLDeserializer()
        );

        u1 = new AdjudicatorAccessReview(new UserContext("u1"), new PrivilegeChecker(pap, new PolicyReviewer(pap)));
        u2 = new AdjudicatorAccessReview(new UserContext("u2"), new PrivilegeChecker(pap, new PolicyReviewer(pap)));
    }

    @Test
    void testGetPrivileges() {
        assertThrows(PMException.class, () -> {
            u2.getPrivileges(new UserContext("u1"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getPrivileges(new UserContext("u2"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getPrivileges(new UserContext("u1"), "o1");
        });
    }

    @Test
    void testGetDeniedPrivileges() {
        assertThrows(PMException.class, () -> {
            u2.getDeniedPrivileges(new UserContext("u1"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getDeniedPrivileges(new UserContext("u2"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getDeniedPrivileges(new UserContext("u1"), "o1");
        });
    }

    @Test
    void testGetPolicyClassAccessRights() {
        assertThrows(PMException.class, () -> {
            u2.getPolicyClassAccessRights(new UserContext("u1"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getPolicyClassAccessRights(new UserContext("u2"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.getPolicyClassAccessRights(new UserContext("u1"), "o1");
        });
    }

    @Test
    void testBuildCapabilityList() {
        assertThrows(PMException.class, () -> {
            u2.buildCapabilityList(new UserContext("u1"));
        });
        assertDoesNotThrow(() -> {
            u1.buildCapabilityList(new UserContext("u2"));
        });
        assertDoesNotThrow(() -> {
            u1.buildCapabilityList(new UserContext("u1"));
        });
    }

    @Test
    void testBuildACL() {
        assertThrows(PMException.class, () -> {
            u2.buildACL("o1");
        });
        assertDoesNotThrow(() -> {
            u1.buildACL("o1");
        });
    }

    @Test
    void testGetBorderAttributes() {
        assertThrows(PMException.class, () -> {
            u2.getBorderAttributes("u1");
        });
        assertDoesNotThrow(() -> {
            u1.getBorderAttributes("u2");
        });
        assertDoesNotThrow(() -> {
            u1.getBorderAttributes("u1");
        });
    }

    @Test
    void testGetSubgraphPrivileges() {
        assertThrows(PMException.class, () -> {
            u2.getBorderAttributes("u1");
        });
        assertDoesNotThrow(() -> {
            u1.getBorderAttributes("u2");
        });
        assertDoesNotThrow(() -> {
            u1.getBorderAttributes("u1");
        });
    }

    @Test
    void testExplain() {
        assertThrows(PMException.class, () -> {
            u2.explain(new UserContext("u1"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.explain(new UserContext("u2"), "o1");
        });
        assertDoesNotThrow(() -> {
            u1.explain(new UserContext("u1"), "o1");
        });
    }

    @Test
    void testBuildPOS() {
        assertThrows(PMException.class, () -> {
            u2.buildPOS(new UserContext("u1"));
        });
        assertDoesNotThrow(() -> {
            u1.buildPOS(new UserContext("u2"));
        });
        assertDoesNotThrow(() -> {
            u1.buildPOS(new UserContext("u1"));
        });
    }

    @Test
    void testGetAccessibleChildren() {
        assertThrows(PMException.class, () -> {
            u2.getAccessibleChildren(new UserContext("u1"), "oa1");
        });
        assertDoesNotThrow(() -> {
            u1.getAccessibleChildren(new UserContext("u2"), "oa1");
        });
        assertDoesNotThrow(() -> {
            u1.getAccessibleChildren(new UserContext("u1"), "oa1");
        });
    }

    @Test
    void testGetAccessibleParents() {
        assertThrows(PMException.class, () -> {
            u2.getAccessibleParents(new UserContext("u1"), "oa1");
        });
        assertDoesNotThrow(() -> {
            u1.getAccessibleParents(new UserContext("u2"), "oa1");
        });
        assertDoesNotThrow(() -> {
            u1.getAccessibleParents(new UserContext("u1"), "oa1");
        });
    }

}