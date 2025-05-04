package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.util.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.util.PolicyEquals.assertPolicyEquals;
import static gov.nist.csd.pm.util.TestIdGenerator.id;


public class SerializationTest {

    @Test
    void testJSON() throws PMException, IOException {
        MemoryPAP pap = new TestPAP();
        SamplePolicy.loadSamplePolicyFromPML(pap);

        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testSerializationWithAdminNodes() throws PMException {
        PAP pap = new TestPAP()
                .withIdGenerator(new TestIdGenerator());

        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]                
                """);
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testSerializationNodeProperties() throws PMException {
        MemoryPAP pap = new TestPAP();
        pap.executePML(new TestUserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                set properties of "ua1" to {"a": "b"}
                """);
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP jsonPAP = new TestPAP();
        jsonPAP.deserialize(json, new JSONDeserializer());

        assertPolicyEquals(pap.query(), jsonPAP.query());
    }
}