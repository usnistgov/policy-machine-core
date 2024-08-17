package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.op.Operation;
import gov.nist.csd.pm.pap.pml.executable.operation.PMLOperationWrapper;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.query.UserContext;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.util.PolicyEquals;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLTest {

    @Test
    void testDeserialization() throws IOException, PMException {
        String pml = SamplePolicy.loadSamplePolicyPML();

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        MemoryPAP pap = new MemoryPAP();
        pap.modify().graph().createPolicyClass("test_pc");
        pap.modify().graph().createUserAttribute("ua1", Collections.singleton("test_pc"));
        pap.modify().graph().createUser("u1", Collections.singleton("ua1"));

        pap.deserialize(new UserContext("u1"), pml, pmlDeserializer);

        String serialize = pap.serialize(new PMLSerializer());
        MemoryPAP pap2 = new MemoryPAP();
        pap2.deserialize(new UserContext("u1"), serialize, pmlDeserializer);

        PolicyEquals.assertPolicyEquals(pap.query(), pap2.query());
    }

    @Test
    void testDeserializationWithCustomFunctions() throws PMException {
        String pml = """
               testFunc("hello world")
               """;

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        MemoryPAP pap = new MemoryPAP();
        assertThrows(PMException.class, () -> pap.deserialize(new UserContext("u1"), pml, pmlDeserializer));

        pap.setPMLOperations(new PMLOperationWrapper(new Operation<>("testFunc", List.of("name")) {
            @Override
            public void canExecute(PAP pap, UserContext userCtx, Map<String, Object> operands) throws PMException {

            }

            @Override
            public Object execute(PAP pap, Map<String, Object> operands) throws PMException {
                pap.modify().graph().createPolicyClass((String) operands.get("name"));
                return null;
            }
        }));

        PMLDeserializer pmlDeserializer2 = new PMLDeserializer();
        pap.deserialize(new UserContext("u1"), pml, pmlDeserializer2);
        assertTrue(pap.query().graph().nodeExists("hello world"));
    }

}