package gov.nist.csd.pm.policy.pml.compiler.visitor;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLContextVisitor;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.antlr.PMLParser;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.statement.IfStatement;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.statement.ShortDeclarationStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IfStmtVisitorTest {

    @Test
    void testSuccess() throws PMException {
        PMLParser.IfStatementContext ctx = PMLContextVisitor.toCtx(
                "if true {\n" +
                        "                    x := \"a\"\n" +
                        "                } else if false {\n" +
                        "                    x := \"b\"\n" +
                        "                } else {\n" +
                        "                    x := \"c\"\n" +
                        "                }",
                PMLParser.IfStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new IfStmtVisitor(visitorCtx)
                .visitIfStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new IfStatement(
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true), List.of(new ShortDeclarationStatement("x",
                                                                                             new StringLiteral("a")
                        ))),
                        List.of(new IfStatement.ConditionalBlock(
                                new BoolLiteral(false), List.of(new ShortDeclarationStatement("x",
                                                                                              new StringLiteral("b")
                        )))),
                        List.of(new ShortDeclarationStatement("x", new StringLiteral("c")))
                ),
                stmt
        );
    }

    @Test
    void testConditionExpressionsNotBool() throws PMException {
        PMLParser.IfStatementContext ctx = PMLContextVisitor.toCtx(
                "" +
                        "if \"a\" {\n" +
                        "                    x := \"a\"\n" +
                        "                } else if \"b\" {\n" +
                        "                    x := \"b\"\n" +
                        "                } else {\n" +
                        "                    x := \"c\"\n" +
                        "                }",
                PMLParser.IfStatementContext.class);
        VisitorContext visitorCtx = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        PMLStatement stmt = new IfStmtVisitor(visitorCtx)
                .visitIfStatement(ctx);
        assertEquals(2, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                "expected expression type bool, got string",
                visitorCtx.errorLog().getErrors().get(0).errorMessage()
        );
        assertEquals(
                "expected expression type bool, got string",
                visitorCtx.errorLog().getErrors().get(1).errorMessage()
        );
    }

    @Test
    void testReturnVoidInIf() throws PMException {
        String pml =
                "function f1() {\n" +
                "    if true {\n" +
                "        return\n" +
                "    }\n" +
                "    \n" +
                "    create policy class \"pc1\"\n" +
                "}\n" +
                "\n" +
                "f1()";
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext(), pml);
        assertFalse(store.graph().nodeExists("pc1"));
    }

}