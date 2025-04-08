package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.function.arg.Args;
import gov.nist.csd.pm.pap.function.op.Operation;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PrivilegeChecker;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.util.PolicyEquals;
import gov.nist.csd.pm.util.SamplePolicy;
import gov.nist.csd.pm.util.TestPAP;
import gov.nist.csd.pm.util.TestUserContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static gov.nist.csd.pm.pap.PAPTest.ARG_A;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PMLTest {

    @Test
    void testDeserialization() throws IOException, PMException {
        String pml = SamplePolicy.loadSamplePolicyPML();

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        MemoryPAP pap = new TestPAP();
        long pc1 = pap.modify().graph().createPolicyClass("test_pc");
        long ua1 = pap.modify().graph().createUserAttribute("ua1", Collections.singleton(pc1));
        pap.modify().graph().createUser("u1", Collections.singleton(ua1));

        pap.deserialize(new TestUserContext("u1"), pml, pmlDeserializer);

        String serialize = pap.serialize(new PMLSerializer());
        MemoryPAP pap2 = new TestPAP();
        pap2.deserialize(new TestUserContext("u1"), serialize, pmlDeserializer);

        PolicyEquals.assertPolicyEquals(pap.query(), pap2.query());
    }

    @Test
    void testDeserializationWithCustomFunctions() throws PMException {
        String pml = """
               testFunc("hello world")
               """;

        PMLDeserializer pmlDeserializer = new PMLDeserializer();

        MemoryPAP pap = new TestPAP();
        assertThrows(PMException.class, () -> pap.deserialize(new TestUserContext("u1"), pml, pmlDeserializer));

        pap.setPMLOperations(new PMLOperationWrapper(new Operation<>("testFunc", List.of(ARG_A)) {
            @Override
            public void canExecute(PrivilegeChecker privilegeChecker, UserContext userCtx, Args args) throws PMException {

            }

            @Override
            public Object execute(PAP pap, Args args) throws PMException {
                pap.modify().graph().createPolicyClass(args.get(ARG_A));
                return null;
            }
        }));

        PMLDeserializer pmlDeserializer2 = new PMLDeserializer();
        pap.deserialize(new TestUserContext("u1"), pml, pmlDeserializer2);
        assertTrue(pap.query().graph().nodeExists("hello world"));
    }

}