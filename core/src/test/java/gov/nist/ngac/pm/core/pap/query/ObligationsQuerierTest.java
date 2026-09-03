/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public abstract class ObligationsQuerierTest extends PAPTestInitializer {

    public Obligation obligation1() throws PMException {
        return new Obligation(
            NodeUserContext.of(id("u1")),
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
            NodeUserContext.of(id("u1")),
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
            NodeUserContext.of(id("u1")),
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

            pap.modify().obligations().createObligation(obligation1);
            pap.modify().obligations().createObligation(obligation2);
            pap.modify().obligations().createObligation(obligation3);

            Collection<Obligation> obligations = pap.query().obligations().getObligations();
            assertEquals(3, obligations.size());
            for (Obligation obligation : obligations) {
                if (obligation.getName().equals(obligation1.getName())) {
                    assertEquals(obligation1, obligation);
                } else if (obligation.getName().equals(obligation2.getName())){
                    assertEquals(obligation2, obligation);
                } else {
                    assertEquals(obligation3, obligation);
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

            pap.modify().obligations().createObligation(obligation1);
            pap.modify().obligations().createObligation(obligation2);
            pap.modify().obligations().createObligation(obligation3);


            Obligation obligation = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, obligation);
        }
    }

}