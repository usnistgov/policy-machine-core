package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.scope.CompileScope;



import gov.nist.csd.pm.pap.query.model.context.UserContext;
import org.junit.jupiter.api.Test;

import static gov.nist.csd.pm.pap.function.arg.type.Type.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceByIDTest {

    @Test
    void testGetType() throws PMException {
        VariableReferenceExpression<?> a = new VariableReferenceExpression<>("a", STRING_TYPE);
        VisitorContext visitorContext = new VisitorContext(new CompileScope());
        visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));

        assertEquals(
                STRING_TYPE,
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        VariableReferenceExpression<?> a = new VariableReferenceExpression<>("a", STRING_TYPE);
        ExecutionContext executionContext = new ExecutionContext(new UserContext(0), new MemoryPAP());
        Object expected = "test";
        executionContext.scope().addVariable("a", expected);

        Object actual = a.execute(executionContext, new MemoryPAP());
        assertEquals(expected, actual);
    }

}