package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.scope.ExecuteGlobalScope;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.GlobalScope;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableDeclarationStatement stmt1 = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("a", new StringLiteral("a")),
                        new VariableDeclarationStatement.Declaration("b", new StringLiteral("b"))
                )
        );

        VariableDeclarationStatement stmt2 = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteral("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteral("d"))
                )
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"), new MemoryPAP());
        ctx.scope().addVariable("c", new StringValue("123"));
        stmt1.execute(ctx, new MemoryPAP());
        stmt2.execute(ctx, new MemoryPAP());

        assertEquals(new StringValue("a"), ctx.scope().getVariable("a"));
        assertEquals(new StringValue("b"), ctx.scope().getVariable("b"));
        assertEquals(new StringValue("c"), ctx.scope().getVariable("c"));
        assertEquals(new StringValue("d"), ctx.scope().getVariable("d"));
    }

    @Test
    void testToFormattedString() {
        VariableDeclarationStatement stmt = new VariableDeclarationStatement(
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteral("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteral("d"))
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