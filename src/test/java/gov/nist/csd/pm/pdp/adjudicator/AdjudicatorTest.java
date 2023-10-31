package gov.nist.csd.pm.pdp.adjudicator;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.pdp.reviewer.PolicyReviewer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjudicatorTest {

    static PAP pap;

    @BeforeAll
    static void setup() throws PMException {
        pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(
                new UserContext("u1"),
                """
                set resource access rights ["read", "write"]
                
                create policy class "pc1" {
                    uas {
                        "ua1"
                        "ua2"
                    }
                    associations {
                        "ua1" and ADMIN_POLICY_TARGET with ["*"]
                    }
                }
                
                create user "u1" assign to ["ua1"]
                create user "u2" assign to ["ua2"]                
                """,
                new PMLDeserializer()
        );
    }

    @Test
    void testSerialize() {
        Adjudicator adjudicator1 = new Adjudicator(new UserContext("u2"), pap, new PolicyReviewer(pap));
        assertThrows(PMException.class, () -> adjudicator1.serialize(new PMLSerializer()));

        Adjudicator adjudicator2 = new Adjudicator(new UserContext("u1"), pap, new PolicyReviewer(pap));
        assertDoesNotThrow(() -> adjudicator2.serialize(new PMLSerializer()));
    }

    @Test
    void testDeserialize() {
        Adjudicator adjudicator1 = new Adjudicator(new UserContext("u2"), pap, new PolicyReviewer(pap));
        assertThrows(PMException.class, () -> adjudicator1.deserialize(new UserContext(), "", new PMLDeserializer()));

        Adjudicator adjudicator2 = new Adjudicator(new UserContext("u1"), pap, new PolicyReviewer(pap));
        assertDoesNotThrow(() -> adjudicator2.deserialize(new UserContext(), "", new PMLDeserializer()));
    }

    @Test
    void testReset() {
        Adjudicator adjudicator1 = new Adjudicator(new UserContext("u2"), pap, new PolicyReviewer(pap));
        assertThrows(PMException.class, () -> adjudicator1.reset());

        Adjudicator adjudicator2 = new Adjudicator(new UserContext("u1"), pap, new PolicyReviewer(pap));
        assertDoesNotThrow(() -> adjudicator2.reset());
    }

}