package gov.nist.csd.pm.core.pap.pml.expression.reference;


import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.function.arg.type.ListType;
import gov.nist.csd.pm.core.pap.function.arg.type.Type;
import gov.nist.csd.pm.core.pap.function.arg.type.MapType;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;

import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.util.TestPAP;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.core.pap.function.arg.type.BasicTypes.STRING_TYPE;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReferenceByDotIndexTest {

    @Test
    void testGetType() throws PMException {
        MapType<String, List<String>> mapType = MapType.of(STRING_TYPE, ListType.of(STRING_TYPE));
        DotIndexExpression<?> a = new DotIndexExpression<>(
            new VariableReferenceExpression<>("a", mapType),
            "b",
            ListType.of(STRING_TYPE)
        );
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", mapType, false));

        assertEquals(
                ListType.of(STRING_TYPE),
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        DotIndexExpression<?> a = new DotIndexExpression<>(
            new VariableReferenceExpression<>("a", MapType.of(STRING_TYPE, ListType.of(STRING_TYPE))),
            "b",
            ListType.of(STRING_TYPE)
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
                
                create PC a.b.c.d
                """;
        PAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", List.of(pc1));
        long u1 = pap.modify().graph().createUserAttribute("u1", List.of(ua1));
        pap.executePML(new UserContext(u1), pml);

        assertTrue(pap.query().graph().nodeExists("e"));
    }


}