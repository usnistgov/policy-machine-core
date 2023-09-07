package gov.nist.csd.pm.pap.memory;

import com.google.gson.Gson;
import gov.nist.csd.pm.pap.PAP;
import gov.nist.csd.pm.pap.PAPTest;
import gov.nist.csd.pm.policy.exceptions.NodeDoesNotExistException;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.AccessRightSet;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.model.graph.nodes.Node;
import gov.nist.csd.pm.policy.model.graph.nodes.Properties;
import gov.nist.csd.pm.policy.model.graph.relationships.Association;
import gov.nist.csd.pm.policy.model.prohibition.ContainerCondition;
import gov.nist.csd.pm.policy.model.prohibition.ProhibitionSubject;
import gov.nist.csd.pm.policy.pml.model.expression.Value;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPAPTest extends PAPTest{

    @Override
    public PAP getPAP() throws PMException {
        return new PAP(new MemoryPolicyStore());
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