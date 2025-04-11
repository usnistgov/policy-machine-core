package gov.nist.csd.pm.pap.pml.statement;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.FormalParameter;
import gov.nist.csd.pm.pap.function.arg.type.VoidType;
import gov.nist.csd.pm.pap.function.op.arg.NodeFormalParameter;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.pap.pml.function.operation.PMLStmtsOperation;
import gov.nist.csd.pm.pap.pml.function.operation.CheckAndStatementsBlock;
import gov.nist.csd.pm.pap.pml.function.routine.PMLStmtsRoutine;
import gov.nist.csd.pm.pap.pml.statement.basic.ReturnStatement;
import gov.nist.csd.pm.pap.pml.statement.operation.*;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static org.junit.jupiter.api.Assertions.*;

class FunctionDefinitionStatementTest {

    private static final FormalParameter<String> a = new NodeFormalParameter<>("a", STRING_TYPE);
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);

    @Test
    void testOperationFormattedString() {
        OperationDefinitionStatement stmt = new OperationDefinitionStatement(new PMLStmtsOperation(
                "op1",
                STRING_TYPE,
                List.of(a, b),
                new CheckAndStatementsBlock(
                        new PMLStatementBlock(
                                new CheckStatement(
                                    new StringLiteralExpression("ar1"),
                                    new VariableReferenceExpression<>("a", listType(STRING_TYPE))
                                ),
                                new CheckStatement(
                                    new StringLiteralExpression("ar2"),
                                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)
                                )
                        ),
                        new PMLStatementBlock(
                                List.of(
                                        new ReturnStatement(new StringLiteralExpression("test"))
                                )
                        )
                )
        ));

        assertEquals("""
                             operation op1(@node string a, string b) string {
                                 check "ar1" on [a]
                                 check "ar2" on ["node"]
                             } {
                                 return "test"
                             }""",
                stmt.toFormattedString(0));

        assertEquals("""
                                 operation op1(@node string a, string b) string {
                                     check "ar1" on [a]
                                     check "ar2" on ["node"]
                                 } {
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
        OperationDefinitionStatement stmt = new OperationDefinitionStatement(new PMLStmtsOperation(
                "func1",
                new VoidType(),
                List.of(a, b),
                new CheckAndStatementsBlock(
                        new PMLStatementBlock(
                                new CheckStatement(
                                    new StringLiteralExpression("ar1"),
                                    ArrayLiteralExpression.of(List.of(new VariableReferenceExpression<>("a", STRING_TYPE)), STRING_TYPE)
                                ),
                                new CheckStatement(
                                    new StringLiteralExpression("ar2"),
                                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)
                                )
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
                                 check "ar1" on [a]
                                 check "ar2" on ["node"]
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