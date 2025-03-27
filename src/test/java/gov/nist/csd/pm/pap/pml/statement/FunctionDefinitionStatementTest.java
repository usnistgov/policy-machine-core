package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.executable.arg.PMLFormalArg;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLNodeFormalArg;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.executable.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.executable.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.*;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionStatementTest {

    private static final PMLNodeFormalArg a = new PMLNodeFormalArg("a", Type.string());
    private static final PMLFormalArg b = new PMLFormalArg("b", Type.string());

    @Test
    void testOperationFormattedString() {
        CreateExecutableStatement stmt = new CreateOperationStatement(new PMLStmtsOperation(
                "op1",
                Type.string(),
                List.of(a, b),
                new CheckAndStatementsBlock(
                        new PMLStatementBlock(
                                new CheckStatement(new StringLiteral("ar1"), new ReferenceByID("a")),
                                new CheckStatement(new StringLiteral("ar2"), new StringLiteral("node"))
                        ),
                        new PMLStatementBlock(
                                List.of(
                                        new ReturnStatement(new StringLiteral("test"))
                                )
                        )
                )
        ));

        assertEquals("""
                             operation op1(@node string a, string b) string {
                                 check "ar1" on a
                                 check "ar2" on "node"
                             } {
                                 return "test"
                             }""",
                stmt.toFormattedString(0));

        assertEquals("""
                                 operation op1(@node string a, string b) string {
                                     check "ar1" on a
                                     check "ar2" on "node"
                                 } {
                                     return "test"
                                 }
                             """,
                stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testRoutineFormattedString() {
        CreateExecutableStatement stmt = new CreateRoutineStatement(new PMLStmtsRoutine(
                "rou1",
                Type.voidType(),
                List.of(a, b),
                new PMLStatementBlock(
                        List.of(
                                new CreatePolicyClassStatement(new StringLiteral("test"))
                        )
                )
        ));

        assertEquals("""
                             routine rou1(string a, string b) {
                                 create PC "test"
                             }""",
                stmt.toFormattedString(0));

        assertEquals("""
                                 routine rou1(string a, string b) {
                                     create PC "test"
                                 }
                             """,
                stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testToFormattedStringVoidReturn() {
        CreateExecutableStatement stmt = new CreateOperationStatement(new PMLStmtsOperation(
                "func1",
                Type.voidType(),
                List.of(a, b),
                new CheckAndStatementsBlock(
                        new PMLStatementBlock(
                                new CheckStatement(new StringLiteral("ar1"), new ReferenceByID("a")),
                                new CheckStatement(new StringLiteral("ar2"), new StringLiteral("node"))
                        ),
                        new PMLStatementBlock(
                                List.of(
                                        new ReturnStatement()
                                )
                        )
                )
        ));

        assertEquals("""
                             operation func1(@node string a, string b) {
                                 check "ar1" on a
                                 check "ar2" on "node"
                             } {
                                 return
                             }""",
                stmt.toFormattedString(0));
    }

    @Test
    void testFormalArgOverwritesVariable()
            throws PMException {
        String pml = """
                var a = "test"
                var b = "test2"
                func1(a, b)
                
                operation func1(string a, string b) {
                    create policy class a
                    create policy class b
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
        assertTrue(pap.query().graph().nodeExists("test2"));
    }

    @Test
    void testInvokeFromDefinition() throws PMException {
        String pml = """
                operation f1(string a) {
                    create policy class a
                }
                
                operation f2() {
                    a := "test"
                    f1(a)
                }
                
                f2()
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
    }

    @Test
    void testUsingVarInOperationThrowsException() throws PMException {
        String pml = """
                x := "x"
                
                operation func2() {
                    create policy class x
                }
                """;
        PAP pap = new TestPAP();
        assertThrows(PMLCompilationException.class, () -> pap.executePML(new UserContext(0), pml));
    }
}