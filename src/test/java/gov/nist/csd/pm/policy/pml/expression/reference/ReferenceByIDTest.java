package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.pml.compiler.Variable;
import gov.nist.csd.pm.policy.pml.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.context.VisitorContext;
import gov.nist.csd.pm.policy.pml.scope.GlobalScope;
import gov.nist.csd.pm.policy.pml.scope.PMLScopeException;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.Value;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReferenceByIDTest {

    @Test
    void testGetType() throws PMException {
        ReferenceByID a = new ReferenceByID("a");
        VisitorContext visitorContext = new VisitorContext(GlobalScope.withVariablesAndSignatures(new MemoryPolicyStore()));
        visitorContext.scope().addVariable("a", new Variable("a", Type.string(), false));

        assertEquals(
                Type.string(),
                a.getType(visitorContext.scope())
        );
    }

    @Test
    void testExecute() throws PMException {
        ReferenceByID a = new ReferenceByID("a");
        ExecutionContext executionContext = new ExecutionContext(new UserContext(""), GlobalScope.withValuesAndDefinitions(new MemoryPolicyStore()));
        Value expected = new StringValue("test");
        executionContext.scope().addVariable("a", expected);

        Value actual = a.execute(executionContext, new MemoryPolicyStore());
        assertEquals(expected, actual);
    }

}