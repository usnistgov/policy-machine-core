package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.ArgType;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.exception.PMLCompilationException;
import gov.nist.csd.pm.pap.pml.expression.literal.ArrayLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.MapLiteralExpression;
import gov.nist.csd.pm.pap.pml.expression.literal.StringLiteralExpression;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.function.arg.type.ArgType.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.listType;
import static gov.nist.csd.pm.pap.function.arg.type.ArgType.mapType;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceByBracketIndexTest {

    @Test
    void testGetType() throws PMException {
        BracketIndexExpression<?> a = new BracketIndexExpression<>(
            new VariableReferenceExpression<>("a", ArgType.mapType(STRING_TYPE, ArgType.listType(STRING_TYPE))),
            new StringLiteralExpression("b"),
            listType(STRING_TYPE)
        );
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", ArgType.mapType(STRING_TYPE, ArgType.listType(STRING_TYPE)), false));

        assertEquals(
                listType(STRING_TYPE),
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        BracketIndexExpression<?> a = new BracketIndexExpression<>(
            new VariableReferenceExpression<>("a", mapType(STRING_TYPE, listType(STRING_TYPE))),
            new StringLiteralExpression("b"),
            listType(STRING_TYPE)
        );
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), new MemoryPAP());
        List<String> expected = List.of("1","2");
        Map<String, List<String>> mapValue = Map.of("b", expected);
        executionContext.scope().addVariable("a", mapValue);

        PAP pap = new TestPAP();
        Object actual = a.execute(executionContext, pap);
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
        PMLCompilationException e = assertThrows(PMLCompilationException.class,
                                                 () -> pap.executePML(new TestUserContext("u1"), pml));
        assertEquals("expected expression type bool, got string", e.getErrors().get(0).errorMessage());
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