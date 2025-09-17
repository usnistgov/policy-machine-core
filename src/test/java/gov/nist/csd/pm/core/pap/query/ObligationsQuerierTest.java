package gov.nist.csd.pm.core.pap.query;

import gov.nist.csd.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.obligation.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.obligation.Rule;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class ObligationsQuerierTest extends PAPTestInitializer {

    public Obligation obligation1() throws PMException {
        return new Obligation(
                id("u1"),
                "obl1",
                List.of(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        new SubjectPattern(),
                                        new OperationPattern("test_event")
                                ),
                                new PMLObligationResponse("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
                                ))
                        )
                )
        );
    }

    public Obligation obligation2() throws PMException {
        return new Obligation(
                id("u1"),
                "label2")
                .addRule(
                        new Rule(
                                "rule1",
                                new EventPattern(
                                        new SubjectPattern(),
                                        new OperationPattern("test_event")
                                ),
                                new PMLObligationResponse("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
                                ))
                        )
                ).addRule(
                        new Rule(
                                "rule2",
                                new EventPattern(
                                        new SubjectPattern(),
                                        new OperationPattern("test_event")
                                ),
                                new PMLObligationResponse("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
                                ))
                        )
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

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getRules());

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

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getRules());

            Obligation obligation = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, obligation);
        }
    }

}