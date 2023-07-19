package gov.nist.csd.pm.pap.memory;

import gov.nist.csd.pm.SamplePolicy;
import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.PolicyEquals;
import gov.nist.csd.pm.policy.exceptions.PMException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPolicyDeserializerTest {

    @Test
    void testDeserialize() throws PMException, IOException {
        MemoryPolicyStore memoryPolicyStore = new MemoryPolicyStore();
        SamplePolicy.loadSamplePolicyFromPML(memoryPolicyStore);

        String json = memoryPolicyStore.serialize().toJSON();

        MemoryPolicyStore actual = new MemoryPolicyStore();
        new MemoryPolicyDeserializer(actual).fromJSON(json);

        PolicyEquals.check(memoryPolicyStore, actual);
    }

}