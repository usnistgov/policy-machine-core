package gov.nist.csd.pm.pap.pml.statement;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.statement.operation.CreatePolicyStatement;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.expression.reference.ReferenceByID;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static gov.nist.csd.pm.pap.pml.PMLUtil.buildArrayLiteral;
import static gov.nist.csd.pm.pap.pml.PMLUtil.buildMapLiteral;
import static org.junit.jupiter.api.Assertions.*;

class ForeachStatementTest {

    @Test
    void testSuccess() throws PMException {
        // array
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(new CreatePolicyStatement(new ReferenceByID("x")))
        );

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        UserContext userContext = new UserContext("u1");

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(5, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(List.of("a", "b", "c")));

        // map with key and value vars
        stmt = new ForeachStatement("x", "y", buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyStatement(new ReferenceByID("x")),
                new CreatePolicyStatement(new ReferenceByID("y"))
        ));

        pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(6, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(List.of("a", "b", "c", "d")));

        // map with key only
        stmt = new ForeachStatement("x", null, buildMapLiteral("a", "b", "c", "d"), List.of(
                new CreatePolicyStatement(new ReferenceByID("x"))
        ));

        pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));

        stmt.execute(new ExecutionContext(userContext, pap), pap);

        assertEquals(4, pap.query().graph().getPolicyClasses().size());
        assertTrue(pap.query().graph().getPolicyClasses().containsAll(List.of("a", "c")));
    }

    @Test
    void testOverwriteValues() throws PMException, UnknownVariableInScopeException {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"), List.of(
                new VariableAssignmentStatement("test", false, new ReferenceByID("x"))
        ));

        PAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createUserAttribute("ua1", List.of("pc1"));
        pap.modify().graph().createUser("u1", List.of("ua1"));
        UserContext userContext = new UserContext("u1");

        ExecutionContext executionContext = new ExecutionContext(userContext, pap);
        executionContext.scope().addVariable("test", new StringValue("test"));
        stmt.execute(executionContext, pap);

        assertEquals(
                "c",
                executionContext.scope().getVariable("test").getStringValue()
        );
    }

    @Test
    void testArrayToFormattedString() {
        ForeachStatement stmt = new ForeachStatement("x", null, buildArrayLiteral("a", "b", "c"),
                                                     List.of(
                                                             new CreatePolicyStatement(new ReferenceByID("x"))
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
                                                             new CreatePolicyStatement(new ReferenceByID("x"))
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
        PAP pap = new MemoryPAP();
        pap.executePML(new UserContext(), pml);

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
        pap = new MemoryPAP();
        pap.executePML(new UserContext(), pml);

        assertTrue(pap.query().graph().nodeExists("1"));
        assertFalse(pap.query().graph().nodeExists("2"));
    }
}