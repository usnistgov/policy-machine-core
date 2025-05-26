package gov.nist.csd.pm.core.pap.serialization.json;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.util.PolicyEquals;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class JSONSerializerTest {

    @Test
    void testSerialize() throws PMException, IOException {
        MemoryPAP pap = new MemoryPAP();
        SamplePolicy.loadSamplePolicyFromJSON(pap);

        String serialize = pap.serialize(new JSONSerializer());
        MemoryPAP pap2 = new MemoryPAP();
        pap2.deserialize(serialize, new JSONDeserializer());
        pap2.serialize(new JSONSerializer());

        PolicyEquals.assertPolicyEquals(pap.query(), pap2.query());
    }

}