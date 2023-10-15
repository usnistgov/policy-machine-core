package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.util.PolicyEquals;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static gov.nist.csd.pm.pdp.SuperUserBootstrapper.SUPER_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONTest {

    @Test
    void testSerialization() throws PMException, IOException {
        String json = IOUtils.resourceToString("json/JSONTest.json", StandardCharsets.UTF_8, JSONTest.class.getClassLoader());

        PAP pap = new PAP(new MemoryPolicyStore());
        pap.deserialize(new UserContext(SUPER_USER), json, new JSONDeserializer());

        String serialize = pap.serialize(new JSONSerializer());

        assertEquals(json, serialize);

        PAP pap2 = new PAP(new MemoryPolicyStore());
        pap2.deserialize(new UserContext(SUPER_USER), serialize, new JSONDeserializer());

        PolicyEquals.assertPolicyEquals(pap, pap2);
    }
}
