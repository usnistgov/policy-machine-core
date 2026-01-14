package gov.nist.csd.pm.core.pap.pml.statement;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.core.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.core.pap.function.op.arg.NodeNameFormalParameter;
import gov.nist.csd.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.function.operation.PMLStmtsAdminOperation;
import gov.nist.csd.pm.core.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CheckStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class FunctionDefinitionStatementTest {

    private static final NodeNameFormalParameter a = new NodeNameFormalParameter("a");
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);

    @Test
    void testOperationFormattedString() {
        AdminOpDefinitionStatement stmt = new AdminOpDefinitionStatement(new PMLStmtsAdminOperation(
            "op1",
            STRING_TYPE,
            List.of(a, b),
            new PMLStatementBlock(
                new CheckStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar1")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new VariableReferenceExpression<>("a", STRING_TYPE)), STRING_TYPE)
                ),
                new CheckStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar2")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)
                ),
                new ReturnStatement(new StringLiteralExpression("test"))
            )
        ));

        assertEquals("""
                             adminop op1(@node string a, string b) string {
                                 check ["ar1"] on [a]
                                 check ["ar2"] on ["node"]
                                 return "test"
                             }""",
            stmt.toFormattedString(0));

        assertEquals("""
                                 adminop op1(@node string a, string b) string {
                                     check ["ar1"] on [a]
                                     check ["ar2"] on ["node"]
                                     return "test"
                                 }
                             """,
            stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testRoutineFormattedString() {
        RoutineDefinitionStatement stmt = new RoutineDefinitionStatement(new PMLStmtsRoutine(
            "rou1",
            new VoidType(),
            List.of(a, b),
            new PMLStatementBlock(
                List.of(
                    new CreatePolicyClassStatement(new StringLiteralExpression("test"))
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
        AdminOpDefinitionStatement stmt = new AdminOpDefinitionStatement(new PMLStmtsAdminOperation(
            "func1",
            new VoidType(),
            List.of(a, b),
            new PMLStatementBlock(
                new CheckStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar1")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new VariableReferenceExpression<>("a", STRING_TYPE)), STRING_TYPE)
                ),
                new CheckStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar2")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)

                ),
                new ReturnStatement()
            )
        ));

        assertEquals("""
                             adminop func1(@node string a, string b) {
                                 check ["ar1"] on [a]
                                 check ["ar2"] on ["node"]
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
                
                adminop func1(string a, string b) {
                    create PC a
                    create PC b
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
                adminop f1(string a) {
                    create PC a
                }
                
                adminop f2() {
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
                
                adminop func2() {
                    create PC x
                }
                """;
        PAP pap = new TestPAP();
        assertThrows(PMLCompilationException.class, () -> pap.executePML(new UserContext(0), pml));
    }
}