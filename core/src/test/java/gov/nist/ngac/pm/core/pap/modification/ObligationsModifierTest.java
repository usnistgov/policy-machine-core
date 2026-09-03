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

package gov.nist.ngac.pm.core.pap.modification;

import static gov.nist.ngac.pm.core.util.SamplePolicy.loadSamplePolicyFromPML;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.ngac.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.ngac.pm.core.common.exception.ObligationNameExistsException;
import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAPTestInitializer;
import gov.nist.ngac.pm.core.pap.obligation.Obligation;
import gov.nist.ngac.pm.core.pap.obligation.event.EventPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.ngac.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.ngac.pm.core.pap.obligation.response.ObligationResponse;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

public abstract class ObligationsModifierTest extends PAPTestInitializer {

    public EventPattern eventPattern = new EventPattern(
        new SubjectPattern(),
        new MatchesOperationPattern("")
    );

    public Obligation obligation1() throws PMException {
        return new Obligation(
            NodeUserContext.of(id("u1")),
            "obl1",
            eventPattern,
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            )
            )
        );
    }

    public Obligation obligation2() throws PMException {
        return new Obligation(
            NodeUserContext.of(id("u1")),
            "label2",
            eventPattern,
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    public Obligation obligation3() throws PMException {
        return new Obligation(
            NodeUserContext.of(id("u1")),
            "label2",
            eventPattern,
            new ObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    @Nested
    class CreateObligation {

        @Test
        void testObligationNameExistsException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();
            pap.modify().obligations().createObligation(obligation1);

            assertThrows(ObligationNameExistsException.class, () -> pap.modify().obligations().createObligation(obligation1));
        }

        @Test
        void testAuthorNodeDoestNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                () -> pap.modify().obligations().createObligation(new Obligation(
                    NodeUserContext.of(id("u1")), "test", eventPattern, new ObligationResponse("", List.of()))));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();
            pap.modify().obligations().createObligation(obligation1);

            assertThrows(ObligationNameExistsException.class,
                () -> pap.modify().obligations().createObligation(new Obligation(
                    obligation1.getAuthor(), obligation1.getName(), new EventPattern(
                    new SubjectPattern(), new AnyOperationPattern()
                ), new ObligationResponse("", List.of()))));

            Obligation actual = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, actual);
        }

        @Test
        void testTx() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> pap.executePML(NodeUserContext.of(id("u1")), """
                create obligation "ob1"
                    when any user
                    performs any operation
                    do(ctx) { }
                
                create obligation "ob2"
                    when any user
                    performs any operation
                    do(ctx) { }
                """));
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.executePML(NodeUserContext.of(id("u1")), """
                    create obligation "ob3"
                        when any user
                        performs any operation
                        do(ctx) { }
                    
                    create obligation "ob4"
                        when any user
                        performs any operation
                        do(ctx) { }
                    
                    """);
                throw new PMException("");
            }));

            assertDoesNotThrow(() -> pap.query().obligations().getObligation("ob1"));
            assertDoesNotThrow(() -> pap.query().obligations().getObligation("ob2"));
            assertThrows(ObligationDoesNotExistException.class, () -> pap.query().obligations().getObligation("ob3"));
            assertThrows(ObligationDoesNotExistException.class, () -> pap.query().obligations().getObligation("ob4"));
        }

        @Test
        void testUsernameContextAsAuthor() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            NodeUserContext authorCtx = NodeUserContext.of("u1");
            pap.modify().obligations().createObligation(
                new Obligation(authorCtx, "obl", eventPattern, new ObligationResponse("evtCtx", List.of())));

            Obligation stored = pap.query().obligations().getObligation("obl");
            assertEquals(authorCtx, stored.getAuthor());
        }
    }

    @Nested
    class DeleteObligation {
        @Test
        public void testSuccess() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.executePML(NodeUserContext.of(id("u1")), """
                    create obligation "ob1"
                        when any user
                        performs any operation
                        do(ctx) { }
                    """);

            pap.modify().obligations().deleteObligation("ob1");

            assertThrows(ObligationDoesNotExistException.class, () -> pap.query().obligations().getObligation("ob1"));
        }

        @Test
        void testTx() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> pap.executePML(NodeUserContext.of(id("u1")), """
                create obligation "ob1"
                    when any user 
                    performs any operation
                    do(ctx) { }
                
                
                create obligation "ob2"
                    when any user 
                    performs any operation
                    do(ctx) { }
                """));
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.modify().obligations().deleteObligation("ob1");
                pap.modify().obligations().deleteObligation("ob2");
                throw new PMException("");
            }));

            assertDoesNotThrow(() -> pap.query().obligations().getObligation("ob1"));
            assertDoesNotThrow(() -> pap.query().obligations().getObligation("ob2"));
        }
    }

    @Nested
    class DeleteNode {

        @Test
        void testDeleteNonExistingObligationDoesNOtThrowExcpetion() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();

            assertDoesNotThrow(() -> pap.modify().obligations().deleteObligation(obligation1.getName()));
        }

        @Test
        void testDeleteObligation() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();
            Obligation obligation2 = obligation2();

            pap.modify().obligations().createObligation(obligation1);
            pap.modify().obligations().createObligation(obligation2);

            pap.modify().obligations().deleteObligation(obligation1.getName());

            assertThrows(ObligationDoesNotExistException.class,
                () -> pap.query().obligations().getObligation(obligation1.getName()));
        }
    }
}