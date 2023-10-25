package gov.nist.csd.pm.pap.serialization;

import gov.nist.csd.pm.pap.AdminPolicy;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.memory.MemoryPolicyStore;
import gov.nist.csd.pm.pap.serialization.json.JSONDeserializer;
import gov.nist.csd.pm.pap.serialization.json.JSONSerializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLDeserializer;
import gov.nist.csd.pm.pap.serialization.pml.PMLSerializer;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.util.SamplePolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static gov.nist.csd.pm.util.PolicyEquals.assertPolicyEquals;

public class SerializationTest {

    @Test
    void testJSONAndPML() throws PMException, IOException {
        PAP pap = new PAP(new MemoryPolicyStore());
        SamplePolicy.loadSamplePolicyFromPML(pap);

        String json = pap.serialize(new JSONSerializer());
        String pml = pap.serialize(new PMLSerializer());

        PAP jsonPAP = new PAP(new MemoryPolicyStore());
        jsonPAP.deserialize(new UserContext("u1"), json, new JSONDeserializer());

        PAP pmlPAP = new PAP(new MemoryPolicyStore());
        pmlPAP.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertPolicyEquals(jsonPAP, pmlPAP);
        assertPolicyEquals(pap, pmlPAP);
        assertPolicyEquals(pap, pmlPAP);
    }

    @Test
    void testPolicyClassTargets() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());

        pap.graph().createPolicyClass("pc1");
        pap.graph().createPolicyClass("pc2");
        pap.graph().assign(AdminPolicy.policyClassTargetName("pc1"), "pc2");
        pap.graph().assign(AdminPolicy.policyClassTargetName("pc2"), "pc1");

        String json = pap.serialize(new JSONSerializer());
        String pml = pap.serialize(new PMLSerializer());

        PAP jsonPAP = new PAP(new MemoryPolicyStore());
        jsonPAP.deserialize(new UserContext("u1"), json, new JSONDeserializer());

        PAP pmlPAP = new PAP(new MemoryPolicyStore());
        pmlPAP.deserialize(new UserContext("u1"), pml, new PMLDeserializer());

        assertPolicyEquals(jsonPAP, pmlPAP);
        assertPolicyEquals(pap, pmlPAP);
        assertPolicyEquals(pap, pmlPAP);
    }

}
