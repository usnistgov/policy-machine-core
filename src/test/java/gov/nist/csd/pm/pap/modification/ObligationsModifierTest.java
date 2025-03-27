package gov.nist.csd.pm.pap.modification;

import gov.nist.csd.pm.common.exception.NodeDoesNotExistException;
import gov.nist.csd.pm.common.exception.ObligationDoesNotExistException;
import gov.nist.csd.pm.common.exception.ObligationNameExistsException;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.common.obligation.EventPattern;
import gov.nist.csd.pm.common.obligation.Obligation;
import gov.nist.csd.pm.common.obligation.Response;
import gov.nist.csd.pm.common.obligation.Rule;
import gov.nist.csd.pm.pap.PAPTestInitializer;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.InOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.operand.NodeOperandPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.InSubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.pap.pml.pattern.subject.UsernamePattern;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.util.SamplePolicy.loadSamplePolicyFromPML;
import static org.junit.jupiter.api.Assertions.*;

public abstract class ObligationsModifierTest extends PAPTestInitializer {

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
                                new Response("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteral("test_pc"))
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
                                new Response("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteral("test_pc"))
                                ))
                        )
                ).addRule(
                        new Rule(
                                "rule2",
                                new EventPattern(
                                        new SubjectPattern(),
                                        new OperationPattern("test_event")
                                ),
                                new Response("evtCtx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteral("test_pc"))
                                ))
                        )
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
            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules());

            assertThrows(ObligationNameExistsException.class, () -> pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules()));
        }

        @Test
        void testAuthorNodeDoestNotExistException() throws PMException {
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(id("u1"), "test", List.of()));
        }

        @Test
        void testEventSubjectNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(
                            id("u1"),
                            "obl1",
                            List.of(new Rule(
                                    "rule1",
                                    new EventPattern(
                                            new SubjectPattern(new InSubjectPattern("ua2")),
                                            new OperationPattern("test_event")
                                    ),
                                    new Response("evtCtx", List.of())
                            ))
                    ));
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(
                            id("u1"),
                            "obl1",
                            List.of(new Rule(
                                    "rule1",
                                    new EventPattern(
                                            new SubjectPattern(new InSubjectPattern("ua3")),
                                            new OperationPattern("test_event")
                                    ),
                                    new Response("evtCtx", List.of())
                            ))
                    ));
        }

        @Test
        void testEventTargetNodeDoesNotExistException() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(
                            id("u1"),
                            "obl1",
                            List.of(new Rule(
                                    "rule1",
                                    new EventPattern(
                                            new SubjectPattern(new UsernamePattern("u1")),
                                            new OperationPattern("test_event"),
                                            Map.of("", List.of(new NodeOperandPattern("oa1")))
                                    ),
                                    new Response("evtCtx", List.of())
                            ))
                    ));
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(
                            id("u1"),
                            "obl1",
                            List.of(new Rule(
                                    "rule1",
                                    new EventPattern(
                                            new SubjectPattern(new UsernamePattern("u1")),
                                            new OperationPattern("test_event"),
                                            Map.of("", List.of(new NodeOperandPattern("oa1")))
                                    ),
                                    new Response("evtCtx", List.of())
                            ))
                    ));
            assertThrows(NodeDoesNotExistException.class,
                    () -> pap.modify().obligations().createObligation(
                            id("u1"),
                            "obl1",
                            List.of(new Rule(
                                    "rule1",
                                    new EventPattern(
                                            new SubjectPattern(new UsernamePattern("u1")),
                                            new OperationPattern("test_event"),
                                            Map.of("", List.of(new InOperandPattern("oa1")))
                                    ),
                                    new Response("evtCtx", List.of())
                            ))
                    ));
        }

        @Test
        void testSuccess() throws PMException {
            pap.modify().graph().createPolicyClass("pc1");
            pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
            pap.modify().graph().createUser("u1", ids("ua1"));

            Obligation obligation1 = obligation1();
            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules());

            assertThrows(ObligationNameExistsException.class,
                    () -> pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), List.of()));

            Obligation actual = pap.query().obligations().getObligation(obligation1.getName());
            assertEquals(obligation1, actual);
        }

        @Test
        void testTx() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> pap.executePML(new UserContext(id("u1")), """
                create obligation "ob1" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) { }
                }
                
                create obligation "ob2" {
                    create rule "r1"
                    when any user
                    performs any operation
                    do(ctx) { }
                }
                """));
            assertThrows(PMException.class, () -> pap.runTx(tx -> {
                pap.executePML(new UserContext(id("u1")), """
                    create obligation "ob3" {
                        create rule "r1"
                        when any user
                        performs any operation
                        do(ctx) { }
                    }
                    
                    create obligation "ob4" {
                        create rule "r1"
                        when any user
                        performs any operation
                        do(ctx) { }
                    }
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
                    create obligation "ob1" {
                        create rule "r1"
                        when any user
                        performs any operation
                        do(ctx) { }
                    }
                    """);

            pap.modify().obligations().deleteObligation("ob1");

            assertThrows(ObligationDoesNotExistException.class, () -> pap.query().obligations().getObligation("ob1"));
        }

        @Test
        void testTx() throws PMException, IOException {
            loadSamplePolicyFromPML(pap);

            pap.runTx(tx -> pap.executePML(new UserContext(id("u1")), """
                create obligation "ob1" {
                    create rule "r1"
                    when any user 
                    performs any operation
                    do(ctx) { }
                }
                
                create obligation "ob2" {
                    create rule "r1"
                    when any user 
                    performs any operation
                    do(ctx) { }
                }
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

            pap.modify().obligations().createObligation(obligation1.getAuthorId(), obligation1.getName(), obligation1.getRules());
            pap.modify().obligations().createObligation(obligation2.getAuthorId(), obligation2.getName(), obligation2.getRules());

            pap.modify().obligations().deleteObligation(obligation1.getName());

            assertThrows(ObligationDoesNotExistException.class,
                    () -> pap.query().obligations().getObligation(obligation1.getName()));
        }
    }
}