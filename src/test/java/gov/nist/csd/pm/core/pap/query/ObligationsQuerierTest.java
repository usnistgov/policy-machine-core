package gov.nist.csd.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ObligationsQuerierTest extends PAPTestInitializer {

    public Obligation obligation1() throws PMException {
        return new Obligation(
            id("u1"),
            "obl1",
            new EventPattern(
                new SubjectPattern(),
                new MatchesOperationPattern("test_event")
            ),
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    public Obligation obligation2() throws PMException {
        return new Obligation(
            id("u1"),
            "label2",
            new EventPattern(
                new SubjectPattern(),
                new MatchesOperationPattern("test_event")
            ),
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    public Obligation obligation3() throws PMException {
        return new Obligation(
            id("u1"),
            "label3",
            new EventPattern(
                new SubjectPattern(),
                new MatchesOperationPattern("test_event")
            ),
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    @Nested
    class GetAll {
        @Test
        void testGetObligations() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            long u1 = pap.modify().graph().createUser("u1", List.of(ua1));

            Obligation obligation1 = obligation1();
            Obligation obligation2 = obligation2();
            Obligation obligation3 = obligation3();

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getEventPattern(), obligation2.getResponse());
            pap.modify().obligations().createObligation(obligation3.getAuthorId(), obligation3.getName(), obligation3.getEventPattern(), obligation3.getResponse());

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
                () -> pap.query().obligations().getObligation("test"));
        }

        @Test
        void testGetObligation() throws PMException {
            long pc1 = pap.modify().graph().createPolicyClass("pc1");
            long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
            pap.modify().graph().createUser("u1", List.of(ua1));

            Obligation obligation1 = obligation1();
            Obligation obligation2 = obligation2();
            Obligation obligation3 = obligation3();

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getEventPattern(), obligation2.getResponse());
            pap.modify().obligations().createObligation(obligation3.getAuthorId(), obligation3.getName(), obligation3.getEventPattern(), obligation3.getResponse());


            Obligation obligation = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, obligation);
        }
    }

}