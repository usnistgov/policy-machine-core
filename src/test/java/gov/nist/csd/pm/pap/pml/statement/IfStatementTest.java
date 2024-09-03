package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.BoolLiteral;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IfStatementTest {

    @Test
    void testSuccess() throws PMException {
        String pml = "operation func1(string s) {\n" +
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
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), pml);

        assertTrue(pap.query().graph().nodeExists("a"));
        assertTrue(pap.query().graph().nodeExists("b"));
        assertTrue(pap.query().graph().nodeExists("c"));
        assertTrue(pap.query().graph().nodeExists("d"));
    }

    @Test
    void testToFormattedStringVoidReturn() {
        IfStatement stmt = new IfStatement(
                new IfStatement.ConditionalBlock(
                        new BoolLiteral(true),
                        new PMLStatementBlock(
                                new CreatePolicyStatement(new StringLiteral("a"))
                        )
                ),
                List.of(
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                new PMLStatementBlock(
                                        new CreatePolicyStatement(new StringLiteral("b"))
                                )
                        ),
                        new IfStatement.ConditionalBlock(
                                new BoolLiteral(true),
                                new PMLStatementBlock(
                                        new CreatePolicyStatement(new StringLiteral("c"))
                                )
                        )
                ),
                new PMLStatementBlock(
                        new CreatePolicyStatement(new StringLiteral("d"))
                )
        );

        assertEquals(
                "if true {\n" +
                        "    create PC \"a\"\n" +
                        "} else if true {\n" +
                        "    create PC \"b\"\n" +
                        "} else if true {\n" +
                        "    create PC \"c\"\n" +
                        "} else {\n" +
                        "    create PC \"d\"\n" +
                        "}",
                     stmt.toFormattedString(0));

        assertEquals(
                "    if true {\n" +
                        "        create PC \"a\"\n" +
                        "    } else if true {\n" +
                        "        create PC \"b\"\n" +
                        "    } else if true {\n" +
                        "        create PC \"c\"\n" +
                        "    } else {\n" +
                        "        create PC \"d\"\n" +
                        "    }\n",
                     stmt.toFormattedString(1) + "\n");
    }
}