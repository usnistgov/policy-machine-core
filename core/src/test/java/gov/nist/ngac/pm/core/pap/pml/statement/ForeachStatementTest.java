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
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.ngac.pm.core.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.ngac.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.ngac.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.ForeachStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.ngac.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeUserContext;
import gov.nist.ngac.pm.core.util.TestPAP;
import java.util.List;
import org.junit.jupiter.api.Test;

class ForeachStatementTest {

    @Test
    void testSuccess() throws PMException {
        // array
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(new CreatePolicyClassStatement(new VariableReferenceExpression<>("x", STRING_TYPE)))
        );

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        UserContext userContext = NodeUserContext.of("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(5, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(ids("a", "b", "c")));

        // map with key and value vars
        stmt = new ForeachStatement("x", "y", buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyClassStatement(new VariableReferenceExpression<>("x", STRING_TYPE)),
                new CreatePolicyClassStatement(new VariableReferenceExpression<>("y", STRING_TYPE))
        ));

        pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(6, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(ids("a", "b", "c", "d")));

        // map with key only
        stmt = new ForeachStatement("x", null, buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyClassStatement(new VariableReferenceExpression<>("x", STRING_TYPE))
        ));

        pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(4, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(ids("a", "c")));
    }

    @Test
    void testOverwriteValues() throws PMException {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"), List.of(
                new VariableAssignmentStatement("test", false, new VariableReferenceExpression<>("x", STRING_TYPE))
        ));

        PAP pap = new TestPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", ids("pc1"));
        pap.modify().graph().createUser("u1", ids("ua1"));
        UserContext userContext = NodeUserContext.of("u1");

        ExecutionContext executionContext = new ExecutionContext(userContext, pap);
        executionContext.scope().addVariable("test", "test");
        stmt.execute(executionContext, pap);

        assertEquals(
                "c",
                executionContext.scope().getVariable("test")
        );
    }

    @Test
    void testArrayToFormattedString() {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(
                                                             new CreatePolicyClassStatement(new VariableReferenceExpression<>("x", STRING_TYPE))
                                                     )
        );

        assertEquals("""
                             foreach x in ["a", "b", "c"] {
                                 create PC x
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 foreach x in ["a", "b", "c"] {
                                     create PC x
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testMapToFormattedString() {
        ForeachStatement stmt = new ForeachStatement("x", "y", buildMapLiteral("a", "b", "c", "d"),
                                                     List.of(
                                                             new CreatePolicyClassStatement(new VariableReferenceExpression<>("x", STRING_TYPE))
                                                     )
        );

        assertEquals("""
                             foreach x, y in {"a": "b", "c": "d"} {
                                 create PC x
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 foreach x, y in {"a": "b", "c": "d"} {
                                     create PC x
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }

    @Test
    void testReturnEndsExecution() throws PMException {
        String pml = """
                f1()
                
                adminop f1() {
                    foreach x in ["1", "2", "3"] {
                        if x == "2" {
                            return
                        }
                        
                        create PC x
                    }
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

        assertTrue(pap.query().graph().nodeExists("1"));
        assertFalse(pap.query().graph().nodeExists("2"));

        pml = """
                f1()
                
                adminop f1() {
                    foreach x, y in {"1": "1", "2": "2"} {
                        if x == "2" {
                            return
                        }
                        
                        create PC x
                    }
                }
                """;
        pap = new TestPAP();
        pap.executePML(NodeUserContext.of(0), pml);

        assertTrue(pap.query().graph().nodeExists("1"));
        assertFalse(pap.query().graph().nodeExists("2"));
    }
}