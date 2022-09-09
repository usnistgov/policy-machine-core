package gov.nist.csd.pm.policy.author.pal.expression;

import gov.nist.csd.pm.policy.author.pal.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.events.CreateObjectAttributeEvent;
import gov.nist.csd.pm.policy.events.EventContext;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.noprops;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueTest {

    @Test
    void testStringToValue() throws PMException {
        Value value = Value.objectToValue("test");
        assertTrue(value.isString());
        assertEquals("test", value.getStringValue());
    }

    @Test
    void testArrayToValue() throws PMException {
        Value value = Value.objectToValue(new String[]{"hello", "world"});
        assertTrue(value.isArray());
        assertEquals(new Value("hello"), value.getArrayValue()[0]);
        assertEquals(new Value("world"), value.getArrayValue()[1]);
    }

    @Test
    void testBooleanToValue() throws PMException {
        Value value = Value.objectToValue(true);
        assertTrue(value.isBoolean());
        assertTrue(value.getBooleanValue());
    }

    @Test
    void testListToValue() throws PMException {
        Value value = Value.objectToValue(Arrays.asList("hello", "world"));
        assertTrue(value.isArray());
        assertEquals(new Value("hello"), value.getArrayValue()[0]);
        assertEquals(new Value("world"), value.getArrayValue()[1]);
    }

    @Test
    void testObjectToValue() throws PMException {
        Value objectToValue = Value.objectToValue(new EventContext(new UserContext("testUser"), "target123",
                new CreateObjectAttributeEvent("testOA", noprops(), "pc1")));
        assertTrue(objectToValue.isMap());

        Value key = new Value("userCtx");
        Value value = objectToValue.getMapValue().get(key);
        assertTrue(value.isMap());
        assertEquals(
                Map.of(new Value("user"), new Value("testUser"), new Value("process"), new Value("")),
                value.getMapValue()
        );

        key = new Value("target");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.isString());
        assertEquals(
                "target123",
                value.getStringValue()
        );

        key = new Value("eventName");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.isString());
        assertEquals(
                CREATE_OBJECT_ATTRIBUTE,
                value.getStringValue()
        );

        key = new Value("event");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.isMap());
        assertEquals(
                Map.of(new Value("name"), new Value("testOA"),
                        new Value("type"), new Value("OA"),
                        new Value("properties"), new Value(new HashMap<>()),
                        new Value("initialParent"), new Value("pc1"),
                        new Value("additionalParents"), new Value(new Value[]{})),
                value.getMapValue()
        );
    }
}