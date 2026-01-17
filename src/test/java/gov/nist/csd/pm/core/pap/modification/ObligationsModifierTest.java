package gov.nist.csd.pm.core.pap.modification;

import static gov.nist.csd.pm.core.pap.function.Operation.NAME_PARAM;
import static gov.nist.csd.pm.core.util.SamplePolicy.loadSamplePolicyFromPML;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gov.nist.csd.pm.core.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.core.common.exception.ObligationNameExistsException;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAPTestInitializer;
import gov.nist.csd.pm.core.pap.function.QueryFunction;
import gov.nist.csd.pm.core.pap.function.arg.Args;
import gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes;
import gov.nist.csd.pm.core.pap.obligation.event.EventPattern;
import gov.nist.csd.pm.core.pap.obligation.Obligation;
import gov.nist.csd.pm.core.pap.obligation.event.operation.AnyOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.operation.MatchesOperationPattern;
import gov.nist.csd.pm.core.pap.obligation.response.PMLObligationResponse;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.obligation.event.operation.OperationPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.InSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.obligation.event.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.obligation.event.subject.UsernamePatternExpression;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLQueryFunction;
import gov.nist.csd.pm.core.pap.pml.function.query.PMLStmtsQueryFunction;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.query.PolicyQuery;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.neo4j.cypher.internal.expressions.functions.E;

public abstract class ObligationsModifierTest extends PAPTestInitializer {

    public EventPattern eventPattern = new EventPattern(
        new SubjectPattern(),
        new MatchesOperationPattern("")
    );

    public Obligation obligation1() throws PMException {
        return new Obligation(
            id("u1"),
            "obl1",
            eventPattern,
            new PMLObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            )
            )
        );
    }

    public Obligation obligation2() throws PMException {
        return new Obligation(
            id("u1"),
            "label2",
            eventPattern,
            new PMLObligationResponse("evtCtx", List.of(
                new CreatePolicyClassStatement(new StringLiteralExpression("test_pc"))
            ))
        );
    }

    public Obligation obligation3() throws PMException {
        return new Obligation(
            id("u1"),
            "label2",
            eventPattern,
            new PMLObligationResponse("evtCtx", List.of(
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
            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse());

            assertThrows(ObligationNameExistsException.class, () -> pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse()));
        }

        @Test
        void testAuthorNodeDoestNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                () -> pap.modify().obligations().createObligation(id("u1"), "test", eventPattern, new PMLObligationResponse("", List.of())));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();
            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse());

            assertThrows(ObligationNameExistsException.class,
                () -> pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), new EventPattern(
                    new SubjectPattern(), new AnyOperationPattern()
                ), new PMLObligationResponse("", List.of())));

            Obligation actual = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, actual);
        }

        @Test
        void testTx() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> pap.executePML(new UserContext(id("u1")), """
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
                pap.executePML(new UserContext(id("u1")), """
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
    }

    @Nested
    class DeleteObligation {
        @Test
        public void testSuccess() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.executePML(new UserContext(id("u1")), """
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

            pap.runTx(tx -> pap.executePML(new UserContext(id("u1")), """
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

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getEventPattern(), obligation1.getResponse());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getEventPattern(), obligation2.getResponse());

            pap.modify().obligations().deleteObligation(obligation1.getName());

            assertThrows(ObligationDoesNotExistException.class,
                () -> pap.query().obligations().getObligation(obligation1.getName()));
        }
    }
}