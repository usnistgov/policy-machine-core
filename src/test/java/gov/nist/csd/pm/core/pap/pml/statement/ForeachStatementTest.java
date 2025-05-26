package gov.nist.csd.pm.core.pap.pml.statement;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.expression.reference.VariableReferenceExpression;
import gov.nist.csd.pm.core.pap.pml.statement.basic.ForeachStatement;
import gov.nist.csd.pm.core.pap.pml.statement.basic.VariableAssignmentStatement;
import gov.nist.csd.pm.core.pap.pml.statement.operation.CreatePolicyClassStatement;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import gov.nist.csd.pm.core.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.core.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.core.pap.pml.PMLUtil.buildMapLiteral;
import static gov.nist.csd.pm.core.util.TestIdGenerator.ids;
import static org.junit.jupiter.api.Assertions.*;

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
        UserContext userContext = new TestUserContext("u1");

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
        UserContext userContext = new TestUserContext("u1");

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
                
                operation f1() {
                    foreach x in ["1", "2", "3"] {
                        if x == "2" {
                            return
                        }
                        
                        create PC x
                    }
                }
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("1"));
        assertFalse(pap.query().graph().nodeExists("2"));

        pml = """
                f1()
                
                operation f1() {
                    foreach x, y in {"1": "1", "2": "2"} {
                        if x == "2" {
                            return
                        }
                        
                        create PC x
                    }
                }
                """;
        pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("1"));
        assertFalse(pap.query().graph().nodeExists("2"));
    }
}