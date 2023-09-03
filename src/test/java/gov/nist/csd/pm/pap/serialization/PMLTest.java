package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.policy.pml.model.expression.Type;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.model.function.FormalArgument;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;
import gov.nist.csd.pm.util.PolicyEquals;
import gov.nist.csd.pm.util.SamplePolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static gov.nist.csd.pm.pap.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLTest {

    @Test
    void testDeserialization() throws IOException, PMException {
        String pml = SamplePolicy.loadSamplePolicyPML();

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext(SUPER_USER), pml, pmlDeserializer);

        String serialize = pap.serialize(new PMLSerializer());
        PAP pap2 = new PAP(new MemoryPolicyStore());
        pap2.deserialize(new UserContext(SUPER_USER), serialize, pmlDeserializer);

        PolicyEquals.check(pap, pap2);
    }

    @Test
    void testDeserializationWithCustomFunctions() throws IOException, PMException {
        String pml = """
               testFunc('hello world')
               """;

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        PAP pap = new PAP(new MemoryPolicyStore());
        assertThrows(PMException.class, () -> pap.deserialize(new UserContext(SUPER_USER), pml, pmlDeserializer));

        FunctionDefinitionStatement testFunc = new FunctionDefinitionStatement(
                "testFunc",
                Type.voidType(),
                List.of(new FormalArgument("name", Type.string())),
                (ctx, policy) -> {
                    policy.graph().createPolicyClass(ctx.scope().getValue("name").getStringValue());

                    return new Value();
                }
        );

        PMLDeserializer pmlDeserializer2 = new PMLDeserializer(testFunc);
        pap.deserialize(new UserContext(SUPER_USER), pml, pmlDeserializer2);
        assertTrue(pap.graph().nodeExists("hello world"));

    }

}