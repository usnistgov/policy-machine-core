package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceByBracketIndexTest {

    @Test
    void testGetType() throws PMException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"), new StringLiteral("b"));
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        Type expected =  Type.array(Type.string());
        visitorContext.scope().addVariable("a", new Variable("a", Type.map(Type.string(), expected), false));

        assertEquals(
                expected,
                a.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecute() throws PMException {
        ReferenceByBracketIndex a = new ReferenceByBracketIndex(new ReferenceByID("a"),  new StringLiteral("b"));
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), new MemoryPAP());
        ArrayValue expected = new ArrayValue(List.of(new StringValue("1"), new StringValue("2")), Type.string());
        MapValue mapValue = new MapValue(Map.of(new StringValue("b"), expected), Type.string(), Type.array(Type.string()));
        executionContext.scope().addVariable("a", mapValue);

        PAP pap = new TestPAP();
        Value actual = a.execute(executionContext, pap);
        assertEquals(expected, actual);
    }

    @Test
    void testIndexChain() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a["b"]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("e"));
    }

    @Test
    void testWrongKeyType() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a[true]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUserAttribute("u1", List.of(ua1));
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
                                                 () -> pap.executePML(new UserContext(u1), pml));
        assertEquals("expected expression type(s) [string], got bool", e.getErrors().get(0).errorMessage());
    }

    @Test
    void testKeyDoesNotExist() throws PMException {
        String pml = """
                a := {
                    "b": {
                        "c": {
                            "d": "e"
                        }  
                    }
                }
                
                create policy class a["z"]["c"]["d"]
                """;
        PAP pap = new TestPAP();
        assertThrows(NullPointerException.class,
                     () -> pap.executePML(new UserContext(0), pml));
    }

    @Test
    void testArrayKey() throws PMException {
        String pml = """
                a := {
                    ["a"]: "test"
                }
                
                create policy class a[["a"]]
                """;
        PAP pap = new TestPAP();
        pap.executePML(new UserContext(0), pml);

        assertTrue(pap.query().graph().nodeExists("test"));
    }

}