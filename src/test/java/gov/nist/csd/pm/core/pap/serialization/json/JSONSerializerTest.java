package gov.nist.csd.pm.core.pap.serialization.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.Gson;
import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.impl.memory.pap.MemoryPAP;
import gov.nist.csd.pm.core.util.PolicyEquals;
import gov.nist.csd.pm.core.util.SamplePolicy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    @Test
    void testSerializeDoesNotIncludeNullFields() throws PMException {
        MemoryPAP pap = new MemoryPAP();
        long pc1 = pap.modify().graph().createPolicyClass("pc1");
        pap.modify().graph().createObjectAttribute("oa1", List.of(pc1));
        String serialize = pap.serialize(new JSONSerializer());
        Map map = new Gson().fromJson(serialize, Map.class);
        assertNull(map.get("prohibitions"));
        assertNull(map.get("obligations"));
        assertNull(map.get("operations"));
        assertNull(map.get("routines"));

        Map graph = (Map) map.get("graph");
        assertNull(graph.get("uas"));
        assertNull(graph.get("users"));
        assertNull(graph.get("objects"));
        assertNull(((Map)((List) graph.get("oas")).getFirst()).get("associations"));
    }
}