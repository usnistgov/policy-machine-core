package gov.nist.csd.pm.core.pap.pml.compiler.visitor;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.pml.PMLCompiler;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.OperationPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.arg.AnyArgPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.LogicalSubjectPatternExpression;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.SubjectPattern;
import gov.nist.csd.pm.core.pap.pml.pattern.subject.UsernamePattern;
import gov.nist.csd.pm.core.pap.pml.statement.PMLStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateObligationStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreateRuleStatement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateRuleStmtVisitorTest {

    PMLCompiler pmlCompiler = new PMLCompiler();
    
    @Test
    void testSubjectClause() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any user"
                        when any user
                        performs "test_event"
                        do(ctx) {}
                        
                        create rule "users"
                        when user "u1"
                        performs "test_event"
                        do(ctx) {}
                        
                        create rule "users list"
                        when user "u1" || "u2"
                        performs "test_event"
                        do(ctx) {}
                    }
                    """;
        List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pml);
        assertEquals(1, stmts.size());

        CreateObligationStatement stmt = (CreateObligationStatement)stmts.get(0);

        assertEquals(
                new CreateObligationStatement(
                        new StringLiteralExpression("obligation1"),
                        List.of(
                                new CreateRuleStatement(
                                        new StringLiteralExpression("any user"),
                                        new SubjectPattern(),
                                        new OperationPattern("test_event"),
                                        Map.of(),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                                ),
                                new CreateRuleStatement(
                                        new StringLiteralExpression("users"),
                                        new SubjectPattern(new UsernamePattern("u1")),
                                        new OperationPattern("test_event"),
                                        Map.of(),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                                ),
                                new CreateRuleStatement(
                                        new StringLiteralExpression("users list"),
                                        new SubjectPattern(new LogicalSubjectPatternExpression(new UsernamePattern("u1"), new UsernamePattern("u2"), false)),
                                        new OperationPattern("test_event"),
                                        Map.of(),
                                        new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                                )
                        )
                ),
                stmt
        );
    }

    @Test
    void testPerformsClause() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "r1"
                        when any user
                        performs any operation
                        do(ctx) {}
                    }
                    """;
        List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pml);
        assertEquals(1, stmts.size());

        CreateObligationStatement stmt = (CreateObligationStatement)stmts.get(0);
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteralExpression("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteralExpression("r1"),
                                new SubjectPattern(),
                                new OperationPattern(),
                                Map.of(),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        )
                )
        );
        assertEquals(expected, stmt);

        String pml2 = """
            create obligation "obligation1" {
                create rule "r1"
                when any user
                do(ctx) {}
            }
            """;
        PMLCompilationException e = assertThrows(
                PMLCompilationException.class,
                () -> pmlCompiler.compilePML(pml2)
        );
        assertEquals(1, e.getErrors().size());
        assertEquals("mismatched input 'do' expecting 'performs'", e.getErrors().getFirst().errorMessage());
    }

    @Test
    void testOnClause() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any arg"
                        when any user
                        performs any operation
                        do(ctx) {}
                        
                        create rule "any arg with on"
                        when any user
                        performs any operation
                        on {}
                        do(ctx) {}
                        
                        create rule "an arg"
                        when any user
                        performs "assign"
                        on {
                            ascendant: any
                        }
                        do(ctx) {}
                    }
                    """;
        List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pml);
        assertEquals(1, stmts.size());

        CreateObligationStatement stmt = (CreateObligationStatement)stmts.get(0);
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteralExpression("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteralExpression("any arg"),
                                new SubjectPattern(),
                                new OperationPattern(),
                                Map.of(),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        ),
                        new CreateRuleStatement(
                                new StringLiteralExpression("any arg with on"),
                                new SubjectPattern(),
                                new OperationPattern(),
                                Map.of(),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        ),
                        new CreateRuleStatement(
                                new StringLiteralExpression("an arg"),
                                new SubjectPattern(),
                                new OperationPattern("assign"),
                                Map.of(
                                        "ascendant", List.of(new AnyArgPattern())
                                ),
                                new CreateRuleStatement.ResponseBlock("ctx", new ArrayList<>())
                        )
                )
        );

        assertEquals(expected, stmt);
    }

    @Test
    void testResponse() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "r1"
                        when any user
                        performs any operation
                        do(ctx) {
                            create PC "pc1"
                            create PC "pc2"
                        }
                    }
                    """;
        List<PMLStatement<?>> stmts = pmlCompiler.compilePML(pml);
        assertEquals(1, stmts.size());

        CreateObligationStatement stmt = (CreateObligationStatement)stmts.getFirst();
        CreateObligationStatement expected = new CreateObligationStatement(
                new StringLiteralExpression("obligation1"),
                List.of(
                        new CreateRuleStatement(
                                new StringLiteralExpression("r1"),
                                new SubjectPattern(),
                                new OperationPattern(),
                                Map.of(),
                                new CreateRuleStatement.ResponseBlock("ctx", List.of(
                                        new CreatePolicyClassStatement(new StringLiteralExpression("pc1")),
                                        new CreatePolicyClassStatement(new StringLiteralExpression("pc2"))
                                ))
                        )
                )
        );
        assertEquals(expected, stmt);
    }

    @Test
    void testFunctionInResponseOk() throws PMException {
        String pml = """
                    create obligation "obligation1" {
                        create rule "e1 and e2"
                        when any user
                        performs any operation
                        do(ctx) {
                            operation f1() {}
                        }
                    }
                    """;
        assertDoesNotThrow(() -> pmlCompiler.compilePML(pml));
    }

    @Test
    void testReturnValueInResponseThrowsException() {
        String pml = """
                    create obligation "obligation1" {
                        create rule "any user"
                        when subject => pAny()
                        performs op => pEquals(op, "test_event")
                        do(ctx) {
                            return "test"
                        }
                    }
                    """;
        assertThrows(PMLCompilationException.class, () -> pmlCompiler.compilePML(pml));
    }
}
