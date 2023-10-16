package gov.nist.csd.pm.policy.pml.statement;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.PMLExecutor;
import gov.nist.csd.pm.policy.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IfStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = "function func1(string s) {\n" +
                "                    if s == \"a\" {\n" +
                "                        create policy class s\n" +
                "\n" +
                "                    } else if s == \"b\" {\n" +
                "                        create policy class s\n" +
                "                    \n" +
                "                    } else if s == \"c\" {\n" +
                "                        create policy class s\n" +
                "                    \n" +
                "                    } else {\n" +
                "                        create policy class s\n" +
                "                    \n" +
                "                    }\n" +
                "                }\n" +
                "                \n" +
                "                func1(\"a\")\n" +
                "                func1(\"b\")\n" +
                "                func1(\"c\")\n" +
                "                func1(\"d\")";
        MemoryPolicyStore store = new MemoryPolicyStore();
        PMLExecutor.compileAndExecutePML(store, new UserContext("u1"), pml);

        assertTrue(store.graph().nodeExists("a"));
        assertTrue(store.graph().nodeExists("b"));
        assertTrue(store.graph().nodeExists("c"));
        assertTrue(store.graph().nodeExists("d"));
    }

    @Test
    void testToFormattedStringVoidReturn() {
        IfStatement stmt = new IfStatement(
                new IfStatement.ConditionalBlock(
                        new BoolLiteral(true),
                        List.of(
                                new CreatePolicyStatement(new StringLiteral("a"))
                        )
                ),
                List.of(
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                List.of(
                                        new CreatePolicyStatement(new StringLiteral("b"))
                                )
                        ),
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                List.of(
                                        new CreatePolicyStatement(new StringLiteral("c"))
                                )
                        )
                ),
                List.of(
                        new CreatePolicyStatement(new StringLiteral("d"))
                )
        );

        assertEquals("if true {\n" +
                             "    create PC \"a\"\n" +
                             "} else if true {\n" +
                             "    create PC \"b\"\n" +
                             "} else if true {\n" +
                             "    create PC \"c\"\n" +
                             "} else {\n" +
                             "    create PC \"d\"\n" +
                             "}",
                     stmt.toFormattedString(0));

        assertEquals("    if true {\n" +
                             "        create PC \"a\"\n" +
                             "    } else if true {\n" +
                             "        create PC \"b\"\n" +
                             "    } else if true {\n" +
                             "        create PC \"c\"\n" +
                             "    } else {\n" +
                             "        create PC \"d\"\n" +
                             "    }",
                     stmt.toFormattedString(1));
    }
}