package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariableDeclarationStatementTest {

    @Test
    void testSuccess() throws PMException {
        VariableDeclarationStatement stmt1 = new VariableDeclarationStatement(
                true,
                List.of(
                        new VariableDeclarationStatement.Declaration("a", new StringLiteral("a")),
                        new VariableDeclarationStatement.Declaration("b", new StringLiteral("b"))
                )
        );

        VariableDeclarationStatement stmt2 = new VariableDeclarationStatement(
                false,
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteral("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteral("d"))
                )
        );

        ExecutionContext ctx = new ExecutionContext(new UserContext("u1"), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));
        ctx.scope().addVariable("c", new StringValue("123"));
        stmt1.execute(ctx, new MemoryPolicyStore());
        stmt2.execute(ctx, new MemoryPolicyStore());

        assertEquals(new StringValue("a"), ctx.scope().getVariable("a"));
        assertEquals(new StringValue("b"), ctx.scope().getVariable("b"));
        assertEquals(new StringValue("c"), ctx.scope().getVariable("c"));
        assertEquals(new StringValue("d"), ctx.scope().getVariable("d"));
    }

    @Test
    void testToFormattedString() {
        VariableDeclarationStatement stmt = new VariableDeclarationStatement(
                true,
                List.of(
                        new VariableDeclarationStatement.Declaration("a", new StringLiteral("a")),
                        new VariableDeclarationStatement.Declaration("b", new StringLiteral("b"))
                )
        );

        String expected =
                "const (\n" +
                "    a = \"a\"\n" +
                "    b = \"b\"\n" +
                ")";
        assertEquals(expected, stmt.toFormattedString(0));

        expected =
                "    const (\n" +
                "        a = \"a\"\n" +
                "        b = \"b\"\n" +
                "    )";
        assertEquals(expected, stmt.toFormattedString(1));

        stmt = new VariableDeclarationStatement(
                false,
                List.of(
                        new VariableDeclarationStatement.Declaration("c", new StringLiteral("c")),
                        new VariableDeclarationStatement.Declaration("d", new StringLiteral("d"))
                )
        );

        expected =
                "var (\n" +
                "    c = \"c\"\n" +
                "    d = \"d\"\n" +
                ")";
        assertEquals(expected, stmt.toFormattedString(0));

        expected =
                "    var (\n" +
                "        c = \"c\"\n" +
                "        d = \"d\"\n" +
                "    )";
        assertEquals(expected, stmt.toFormattedString(1));
    }

}