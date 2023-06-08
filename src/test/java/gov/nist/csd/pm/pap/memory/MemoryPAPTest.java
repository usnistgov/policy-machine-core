package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPAPTest {

    @Test
    void testTx() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.beginTx();
        pap.graph().createPolicyClass("pc1");
        pap.rollback();
        assertFalse(pap.graph().nodeExists("pc1"));

        pap.beginTx();
        pap.graph().createPolicyClass("pc1");
        pap.commit();
        assertTrue(pap.graph().nodeExists("pc1"));
    }

    @Test
    void testJsonAsPropertyValue() throws PMException {
        PAP pap = new PAP(new MemoryPolicyStore());
        pap.graph().createPolicyClass("name", Properties.toProperties("test", "{\"12\": \"34\"}", "a", "[\"1\", \"2\"]"));
        Node name = pap.graph().getNode("name");
        Map<String, String> properties = name.getProperties();
        String test = properties.get("test");
        Map m = new Gson().fromJson(test, Map.class);
        assertEquals("34", m.get("12"));

        String json = String.valueOf(properties.get("a"));
        assertArrayEquals(new String[]{"1", "2"}, new Gson().fromJson(json, String[].class));
    }
}