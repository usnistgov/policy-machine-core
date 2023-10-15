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
        String pml = """
                function func1(string s) {
                    if s == "a" {
                        create policy class s

                    } else if s == "b" {
                        create policy class s
                    
                    } else if s == "c" {
                        create policy class s
                    
                    } else {
                        create policy class s
                    
                    }
                }
                
                func1("a")
                func1("b")
                func1("c")
                func1("d")
                """;
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

        assertEquals("""
                             if true {
                                 create PC "a"
                             } else if true {
                                 create PC "b"                          
                             } else if true {
                                 create PC "c"
                             } else {
                                 create PC "d"                             
                             }""",
                     stmt.toFormattedString(0));

        assertEquals("""
                                 if true {
                                     create PC "a"
                                 } else if true {
                                     create PC "b"                          
                                 } else if true {
                                     create PC "c"
                                 } else {
                                     create PC "d"                             
                                 }
                             """,
                     stmt.toFormattedString(1) + "\n");
    }

}