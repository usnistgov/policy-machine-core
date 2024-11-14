package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.util.PolicyEquals;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.util.PolicyEquals.assertPolicyEquals;

public class SerializationTest {

    @Test
    void testJSONAndPML() throws PMException, IOException {
        MemoryPAP pap = new MemoryPAP();
        SamplePolicy.loadSamplePolicyFromPML(pap);

        String json = pap.serialize(new JSONSerializer());
        String pml = pap.serialize(new PMLSerializer());

        MemoryPAP jsonPAP = new MemoryPAP();
        jsonPAP.deserialize(new UserContext("u1"), json, new JSONDeserializer());

        PAP pmlPAP = new MemoryPAP();
        pmlPAP.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertPolicyEquals(jsonPAP.query(), pmlPAP.query());
        assertPolicyEquals(pap.query(), pmlPAP.query());
        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testPMLAndJson() throws PMException, IOException {
        MemoryPAP pml = new MemoryPAP();
        MemoryPAP json = new MemoryPAP();

        SamplePolicy.loadSamplePolicyFromPML(pml);
        SamplePolicy.loadSamplePolicyFromJSON(json);

        PolicyEquals.assertPolicyEquals(pml.query(), json.query());

        String pmlStr = pml.serialize(new PMLSerializer());
        String jsonStr = json.serialize(new JSONSerializer());

        pml.reset();
        pml.deserialize(new UserContext("u1"), pmlStr, new PMLDeserializer());
        json.reset();
        json.deserialize(new UserContext("u1"), pmlStr, new PMLDeserializer());
        PolicyEquals.assertPolicyEquals(pml.query(), json.query());

        pml.reset();
        pml.deserialize(new UserContext("u1"), jsonStr, new JSONDeserializer());
        json.reset();
        json.deserialize(new UserContext("u1"), jsonStr, new JSONDeserializer());
        PolicyEquals.assertPolicyEquals(pml.query(), json.query());
    }

    @Test
    void testSerializationWithAdminNodes() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                associate "ua1" and PM_ADMIN_OBJECT with ["*a"]                
                """);
        String pml = pap.serialize(new PMLSerializer());
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP pmlPAP = new MemoryPAP();
        pmlPAP.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        MemoryPAP jsonPAP = new MemoryPAP();
        jsonPAP.deserialize(new UserContext("u1"), json, new JSONDeserializer());

        assertPolicyEquals(pmlPAP.query(), jsonPAP.query());
        assertPolicyEquals(pap.query(), pmlPAP.query());
        assertPolicyEquals(pap.query(), jsonPAP.query());
    }

    @Test
    void testSerializationNodeProperties() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        pap.executePML(new UserContext("u1"), """
                create pc "pc1"
                create ua "ua1" in ["pc1"]
                set properties of "ua1" to {"a": "b"}
                """);
        String pml = pap.serialize(new PMLSerializer());
        String json = pap.serialize(new JSONSerializer());

        MemoryPAP pmlPAP = new MemoryPAP();
        pmlPAP.deserialize(new UserContext("u1"), pml, new PMLDeserializer());
        MemoryPAP jsonPAP = new MemoryPAP();
        jsonPAP.deserialize(new UserContext("u1"), json, new JSONDeserializer());

        assertPolicyEquals(pmlPAP.query(), jsonPAP.query());
    }
}
