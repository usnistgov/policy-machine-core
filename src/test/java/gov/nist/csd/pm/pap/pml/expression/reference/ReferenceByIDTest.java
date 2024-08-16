package gov.nist.csd.pm.pap.pml.expression.reference;


import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.pml.scope.CompileGlobalScope;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.pml.compiler.Variable;
import gov.nist.csd.pm.pap.pml.context.ExecutionContext;
import gov.nist.csd.pm.pap.pml.context.VisitorContext;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReferenceByIDTest {

    @Test
    void testGetType() throws PMException {
        ReferenceByID a = new ReferenceByID("a");
        VisitorContext visitorContext = new VisitorContext(new CompileGlobalScope());
        visitorContext.scope().addVariable("a", new Variable("a", Type.string(), false));

        assertEquals(
                Type.string(),
                a.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecute() throws PMException {
        ReferenceByID a = new ReferenceByID("a");
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), new MemoryPAP());
        Value expected = new StringValue("test");
        executionContext.scope().addVariable("a", expected);

        Value actual = a.execute(executionContext, new MemoryPAP());
        assertEquals(expected, actual);
    }

}