package gov.nist.csd.pm.core.pap.pml.expression.reference;


import static gov.nist.csd.pm.core.pap.operation.arg.type.BasicTypes.STRING_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.pap.pml.compiler.Variable;
import gov.nist.csd.pm.core.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.core.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.core.pap.pml.scope.CompileScope;
import org.junit.jupiter.api.Test;
import gov.nist.csd.pm.core.pap.query.model.context.NodeUserContext;

class ReferenceByIDTest {

    @Test
    void testGetType() throws PMException {
        VariableReferenceExpression<?> a = new VariableReferenceExpression<>("a", STRING_TYPE);
        VisitorContext visitorContext = new VisitorContext(new CompileScope(new MemoryPAP()));
        visitorContext.scope().addVariable("a", new Variable("a", STRING_TYPE, false));

        assertEquals(
                STRING_TYPE,
                a.getType()
        );
    }

    @Test
    void testExecute() throws PMException {
        VariableReferenceExpression<?> a = new VariableReferenceExpression<>("a", STRING_TYPE);
        ExecutionContext executionContext = new ExecutionContext(NodeUserContext.of(0), new MemoryPAP());
        Object expected = "test";
        executionContext.scope().addVariable("a", expected);

        Object actual = a.execute(executionContext, new MemoryPAP());
        assertEquals(expected, actual);
    }

}