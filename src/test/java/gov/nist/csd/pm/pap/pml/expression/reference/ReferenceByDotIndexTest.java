package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.function.arg.type.Type;
import gov.nist.csd.pm.pap.function.arg.type.MapType;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;

import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static gov.nist.csd.pm.pap.function.arg.type.Type.listType;
import static gov.nist.csd.pm.pap.function.arg.type.Type.mapType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReferenceByDotIndexTest {

    @Test
    void testGetType() throws PMException {
        MapType<String, List<String>> mapType = Type.mapType(STRING_TYPE, listType(STRING_TYPE));
        DotIndexExpression<?> a = new DotIndexExpression<>(
            new VariableReferenceExpression<>("a", mapType),
            "b",
            listType(STRING_TYPE)
        );
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", mapType, false));

        assertEquals(
                listType(STRING_TYPE),
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        DotIndexExpression<?> a = new DotIndexExpression<>(
            new VariableReferenceExpression<>("a", mapType(STRING_TYPE, listType(STRING_TYPE))),
            "b",
            listType(STRING_TYPE)
        );
        PAP pap = new TestPAP();
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), pap);
        List<String> expected = List.of("1","2");
        Map<String, List<String>> mapValue = Map.of("b", expected);

        executionContext.scope().addVariable("a", mapValue);

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
                
                create policy class a.b.c.d
                """;
        PAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUserAttribute("u1", List.of(ua1));
        pap.executePML(new UserContext(u1), pml);

        assertTrue(pap.query().graph().nodeExists("e"));
    }


}