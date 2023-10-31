package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.events.PolicyEvent;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjudicatorObligationsReviewTest {

    static AdjudicatorObligationsReview u1;
    static AdjudicatorObligationsReview u2;

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
                        "ua1" and ADMIN_POLICY_TARGET with [review_policy]
                    }
                }
                
                create user "u1" assign to ["ua1", "ua2"]
                create user "u2" assign to ["ua2"]    
                
                create o "o1" assign to ["oa1-1", "oa1-2"]            
                """,
                new PMLDeserializer()
        );

        u1 = new AdjudicatorObligationsReview(new UserContext("u1"), new PrivilegeChecker(pap, new PolicyReviewer(pap)));
        u2 = new AdjudicatorObligationsReview(new UserContext("u2"), new PrivilegeChecker(pap, new PolicyReviewer(pap)));
    }

    @Test
    void testSuccess() {
        assertDoesNotThrow(() -> {
            u1.getObligationsWithAuthor(new UserContext("u1"));
            u1.getObligationsWithAuthor(new UserContext("u2"));
        });

        assertDoesNotThrow(() -> {
            u1.getObligationsWithAttributeInEvent("oa1");
        });

        assertDoesNotThrow(() -> {
            u1.getObligationsWithAttributeInResponse("oa1");
        });

        assertDoesNotThrow(() -> {
            u1.getObligationsWithEvent("event");
        });

        assertDoesNotThrow(() -> {
            u1.getMatchingEventResponses(new EventContext(null, (PolicyEvent) () -> ""));
        });
    }

    @Test
    void testFail() {
        assertThrows(PMException.class, () -> {
            u2.getObligationsWithAuthor(new UserContext("u1"));
        });

        assertThrows(PMException.class, () -> {
            u2.getObligationsWithAttributeInEvent("oa1");
        });

        assertThrows(PMException.class, () -> {
            u2.getObligationsWithAttributeInResponse("oa1");
        });

        assertThrows(PMException.class, () -> {
            u2.getObligationsWithEvent("event");
        });

        assertThrows(PMException.class, () -> {
            u2.getMatchingEventResponses(new EventContext(null, (PolicyEvent) () -> ""));
        });
    }

}