package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperationBody;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.statement.operation.*;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.type.Type;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionStatementTest {

    @Test
    void testOperationFormattedString() {
        CreateFunctionStatement stmt = new CreateOperationStatement(new PMLStmtsOperation(
                "op1",
                Type.string(),
                List.of("a", "b", "c"),
                List.of("a"),
                Map.of("a", Type.string(), "b", Type.bool(), "c", Type.array(Type.string())),
                new PMLStmtsOperationBody(
                        new PMLStatementBlock(
                                new CheckStatement(new StringLiteral("ar1"), new ReferenceByID("a")),
                                new CheckStatement(new StringLiteral("ar2"), new StringLiteral("node"))
                        ),
                        new PMLStatementBlock(
                                List.of(
                                        new FunctionReturnStatement(new StringLiteral("test"))
                                )
                        )
                )
        ));

        assertEquals("operation op1(nodeop string a, bool b, []string c) string {\n" +
                        "    check \"ar1\" on a\n" +
                        "    check \"ar2\" on \"node\"\n" +
                        "} {\n" +
                        "    return \"test\"\n" +
                        "}",
                stmt.toFormattedString(0));

        assertEquals("    operation op1(nodeop string a, bool b, []string c) string {\n" +
                        "        check \"ar1\" on a\n" +
                        "        check \"ar2\" on \"node\"\n" +
                        "    } {\n" +
                        "        return \"test\"\n" +
                        "    }\n",
                stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testRoutineFormattedString() {
        CreateFunctionStatement stmt = new CreateRoutineStatement(new PMLStmtsRoutine(
                "rou1",
                Type.voidType(),
                List.of("a", "b", "c"),
                Map.of("a", Type.string(), "b", Type.bool(), "c", Type.array(Type.string())),
                new PMLStatementBlock(
                        List.of(
                                new CreatePolicyStatement(new StringLiteral("test"))
                        )
                )
        ));

        assertEquals("routine rou1(string a, bool b, []string c) {\n" +
                        "    create PC \"test\"\n" +
                        "}",
                stmt.toFormattedString(0));

        assertEquals("    routine rou1(string a, bool b, []string c) {\n" +
                        "        create PC \"test\"\n" +
                        "    }\n" ,
                stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testToFormattedStringVoidReturn() {
        CreateFunctionStatement stmt = new CreateOperationStatement(new PMLStmtsOperation(
                "func1",
                Type.voidType(),
                List.of("a", "b", "c"),
                List.of("a"),
                Map.of("a", Type.string(), "b", Type.bool(), "c", Type.array(Type.string())),
                new PMLStmtsOperationBody(
                        new PMLStatementBlock(
                                new CheckStatement(new StringLiteral("ar1"), new ReferenceByID("a")),
                                new CheckStatement(new StringLiteral("ar2"), new StringLiteral("node"))
                        ),
                        new PMLStatementBlock(
                                List.of(
                                        new FunctionReturnStatement()
                                )
                        )
                )
        ));

        assertEquals("operation func1(nodeop string a, bool b, []string c) {\n" +
                        "    check \"ar1\" on a\n" +
                        "    check \"ar2\" on \"node\"\n" +
                        "} {\n" +
                        "    return\n" +
                        "}",
                stmt.toFormattedString(0));
    }

    @Test
    void testFormalArgOverwritesVariable()
            throws PMException {
        String pml = "                var a = \"test\"\n" +
                "                var b = \"test2\"\n" +
                "                func1(a, b)\n" +
                "                \n" +
                "                operation func1(string a, string b) {\n" +
                "                    create policy class a\n" +
                "                    create policy class b\n" +
                "                }";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testInvokeFromDefinition() throws PMException {
        String pml = "                operation f1(string a) {\n" +
                "                    create policy class a\n" +
                "                }\n" +
                "                \n" +
                "                operation f2() {\n" +
                "                    a := \"test\"\n" +
                "                    f1(a)\n" +
                "                }\n" +
                "                \n" +
                "                f2()";
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(""), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testUsingVarInOperationThrowsException() throws PMException {
        String pml = "                x := \"x\"\n" +
                "                \n" +
                "                operation func2() {\n" +
                "                    create policy class x\n" +
                "                }";
        PAP pap = new MemoryPAP();
        assertThrows(PMLCompilationException.class, () -> pap.executePML(new UserContext(""), pml));
    }
}