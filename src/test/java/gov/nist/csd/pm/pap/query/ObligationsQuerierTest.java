package gov.nist.csd.pm.pap.query;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.common.exception.ObligationDoesNotExistException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static gov.nist.csd.pm.pap.modification.ObligationsModifierTest.obligation1;
import static gov.nist.csd.pm.pap.modification.ObligationsModifierTest.obligation2;
import static org.junit.jupiter.api.Assertions.*;

public abstract class ObligationsQuerierTest extends PAPTestInitializer {

    @Nested
    class GetAll {
        @Test
        void testGetObligations() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));

            pap.modify().obligations().createObligation(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules());
            pap.modify().obligations().createObligation(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules());

            Collection<Obligation> obligations = pap.query().obligations().getObligations();
            assertEquals(2, obligations.size());
            for (Obligation obligation : obligations) {
                if (obligation.getName().equals(obligation1.getName())) {
                    assertEquals(obligation1, obligation);
                } else {
                    assertEquals(obligation2, obligation);
                }
            }
        }
    }

    @Nested
    class Get {

        @Test
        void testObligationDoesNotExistException() {
            assertThrows(
                    ObligationDoesNotExistException.class,
                    () -> pap.query().obligations().getObligation(obligation1.getName()));
        }

        @Test
        void testGetObligation() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
            pap.modify().graph().createUser("u1", List.of("ua1"));

            pap.modify().obligations().createObligation(obligation1.getAuthor(), obligation1.getName(), obligation1.getRules());
            pap.modify().obligations().createObligation(obligation2.getAuthor(), obligation2.getName(), obligation2.getRules());

            Obligation obligation = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, obligation);
        }
    }

}