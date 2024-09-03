package gov.nist.csd.pm.pap.pml.compiler.visitor;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.statement.IfStatement;
import gov.nist.csd.pm.pap.pml.statement.PMLStatementBlock;
import gov.nist.csd.pm.pap.pml.statement.ShortDeclarationStatement;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.PMLContextVisitor;
import gov.nist.csd.pm.pap.pml.antlr.PMLParser;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.statement.PMLStatement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.compiler.visitor.CompilerTestUtil.testCompilationError;
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
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());
        PMLStatement stmt = new IfStmtVisitor(visitorCtx)
                .visitIfStatement(ctx);
        assertEquals(0, visitorCtx.errorLog().getErrors().size());
        assertEquals(
                new IfStatement(
                        new IfStatement.ConditionalBlock(new BoolLiteral(true), new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteral("a"))))),
                        List.of(new IfStatement.ConditionalBlock(new BoolLiteral(false), new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteral("b")))))),
                        new PMLStatementBlock(List.of(new ShortDeclarationStatement("x", new StringLiteral("c"))))
                ),
                stmt
        );
    }

    @Test
    void testConditionExpressionsNotBool() throws PMException {
        VisitorContext visitorCtx = new VisitorContext(new CompileGlobalScope());

        testCompilationError(
                "if \"a\" {\n" +
                        "                    x := \"a\"\n" +
                        "                } else if \"b\" {\n" +
                        "                    x := \"b\"\n" +
                        "                } else {\n" +
                        "                    x := \"c\"\n" +
                        "                }", visitorCtx, 1,
                "expected expression type(s) [bool], got string"
                );
    }

    @Test
    void testReturnVoidInIf() throws PMException {
        String pml = "operation f1() {\n" +
                "                    if true {\n" +
                "                        return\n" +
                "                    }\n" +
                "                    \n" +
                "                    create policy class \"pc1\"\n" +
                "                }\n" +
                "                \n" +
                "                f1()";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(), pml);
        assertFalse(pap.query().graph().nodeExists("pc1"));
    }

}