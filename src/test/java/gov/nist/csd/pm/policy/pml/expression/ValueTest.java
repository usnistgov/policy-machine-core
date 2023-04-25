package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.events.CreateObjectAttributeEvent;
import gov.nist.csd.pm.epp.EventContext;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
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
        Value value = Value.objectToValue(List.of("hello", "world"));
        assertTrue(value.isArray());
        assertEquals(new Value("hello"), value.getArrayValue().get(0));
        assertEquals(new Value("world"), value.getArrayValue().get(1));
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
        assertEquals(new Value("hello"), value.getArrayValue().get(0));
        assertEquals(new Value("world"), value.getArrayValue().get(1));
    }

    @Test
    void testObjectToValue() throws PMException {
        EventContext testEventCtx = new EventContext(new UserContext("testUser"), "target123",
                new CreateObjectAttributeEvent("testOA", NO_PROPERTIES, "pc1"));

        Value objectToValue = Value.objectToValue(testEventCtx);
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
                        new Value("additionalParents"), new Value(new ArrayList<>()),
                        new Value("eventName"), new Value("create_object_attribute")
                ),
                value.getMapValue()
        );
    }

    @Test
    void testToObject() throws PMException {
        Value v = new Value("hello world");
        Object o = Value.valueToObject(v);
        assertTrue(o instanceof String);
        assertEquals("hello world", o);

        v = new Value(List.of(new Value("1"), new Value("2")));
        o = Value.valueToObject(v);
        assertTrue(o instanceof List<?>);
        assertEquals(List.of("1", "2"), o);

        v = new Value(List.of(new Value(List.of(new Value("1"), new Value("2")))));
        o = Value.valueToObject(v);
        assertTrue(o instanceof List<?>);
        assertEquals(List.of(List.of("1", "2")), o);
    }
}