package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.statement.basic.VariableDeclarationStatement;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VariableDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableDeclarationStatement stmt1 = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("a", new StringLiteralExpression("a")),
                        new VariableDeclarationStatement.Declaration("b", new StringLiteralExpression("b"))
                )
        );

        VariableDeclarationStatement stmt2 = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteralExpression("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteralExpression("d"))
                )
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext(0), new MemoryPAP());
        ctx.scope().addVariable("c", "123");
        stmt1.execute(ctx, new MemoryPAP());
        stmt2.execute(ctx, new MemoryPAP());

        assertEquals("a", ctx.scope().getVariable("a"));
        assertEquals("b", ctx.scope().getVariable("b"));
        assertEquals("c", ctx.scope().getVariable("c"));
        assertEquals("d", ctx.scope().getVariable("d"));
    }

    @Test
    void testToFormattedString() {
        VariableDeclarationStatement stmt = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteralExpression("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteralExpression("d"))
                )
        );

        String expected = """
                var (
                    c = "c"
                    d = "d"
                )
                """;
        assertEquals(expected, stmt.toFormattedString(0) + "\n");

        expected = """
                    var (
                        c = "c"
                        d = "d"
                    )
                """;
        assertEquals(expected, stmt.toFormattedString(1) + "\n");
    }

}