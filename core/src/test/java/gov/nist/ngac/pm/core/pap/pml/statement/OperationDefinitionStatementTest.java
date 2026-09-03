/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.pml.statement;

import static gov.nist.ngac.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.id;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.arg.type.VoidType;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import gov.nist.ngac.pm.core.pap.operation.param.NodeNameFormalParameter;
import gov.nist.ngac.pm.core.pap.pml.exception.PMLCompilationException;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.ngac.pm.core.pap.pml.operation.admin.PMLStmtsAdminOperation;
import gov.nist.ngac.pm.core.pap.pml.operation.routine.PMLStmtsRoutine;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ReturnStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.AdminOpDefinitionStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RequireStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.RoutineDefinitionStatement;
import gov.nist.ngac.pm.core.pdp.PDP;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;

class OperationDefinitionStatementTest {

    private static final NodeNameFormalParameter a = new NodeNameFormalParameter("a");
    private static final FormalParameter<String> b = new FormalParameter<>("b", STRING_TYPE);

    @Test
    void testOperationFormattedString() {
        AdminOpDefinitionStatement stmt = new AdminOpDefinitionStatement(new PMLStmtsAdminOperation(
            "op1",
            STRING_TYPE,
            List.of(a, b),
            List.of(),
            new PMLStatementBlock(
                new RequireStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar1")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new VariableReferenceExpression<>("a", STRING_TYPE)), STRING_TYPE)
                ),
                new RequireStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar2")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)
                ),
                new ReturnStatement(new StringLiteralExpression("test"))
            )
        ));

        assertEquals("""
                             adminop op1(@Node string a, string b) string {
                                 require ["ar1"] on [a]
                                 require ["ar2"] on ["node"]
                                 return "test"
                             }""",
            stmt.toFormattedString(0));

        assertEquals("""
                                 adminop op1(@Node string a, string b) string {
                                     require ["ar1"] on [a]
                                     require ["ar2"] on ["node"]
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
                             routine rou1(@Node string a, string b) {
                                 create PC "test"
                             }""",
            stmt.toFormattedString(0));

        assertEquals("""
                                 routine rou1(@Node string a, string b) {
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
            List.of(),
            new PMLStatementBlock(
                new RequireStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar1")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new VariableReferenceExpression<>("a", STRING_TYPE)), STRING_TYPE)
                ),
                new RequireStatement(
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("ar2")), STRING_TYPE),
                    ArrayLiteralExpression.of(List.of(new StringLiteralExpression("node")), STRING_TYPE)

                ),
                new ReturnStatement()
            )
        ));

        assertEquals("""
                             adminop func1(@Node string a, string b) {
                                 require ["ar1"] on [a]
                                 require ["ar2"] on ["node"]
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
                func1(a=a, b=b)
                
                adminop func1(string a, string b) {
                    create PC a
                    create PC b
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

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
                    f1(a=a)
                }
                
                f2()
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

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
        assertThrows(PMLCompilationException.class, () -> pap.executePML(NodeUserContext.of(0), pml));
    }

    @Test
    void testFunctionWithRequireStmt() throws PMException {
        String pml = """
            set resource access rights ["read"]
            function f() {
                require ["read"] on ["oa1"]
            }           
            create pc "pc1"
            create ua "ua1" in ["pc1"]
            create ua "ua2" in ["pc1"]
            create oa "oa1" in ["pc1"]
            create u "u1" in ["ua1", "ua2"]
            associate "ua1" to "ua2" with ["*"]
            associate "ua1" to "oa1" with ["*"]
            """;
        TestPAP pap = new TestPAP();
        pap.executePML(null, pml);
        PDP pdp = new PDP(pap);
        pdp.runTx(NodeUserContext.of(id("u1")), pdpTx -> pdpTx.executePML("f()"));
    }
}