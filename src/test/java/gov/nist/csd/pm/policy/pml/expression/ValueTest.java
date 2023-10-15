package gov.nist.csd.pm.policy.pml.expression;

import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.model.access.UserContext;
import gov.nist.csd.pm.policy.events.graph.CreateObjectAttributeEvent;
import gov.nist.csd.pm.epp.EventContext;
import gov.nist.csd.pm.policy.pml.type.Type;
import gov.nist.csd.pm.policy.pml.value.ArrayValue;
import gov.nist.csd.pm.policy.pml.value.MapValue;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;
import org.junit.jupiter.api.Test;

import java.util.*;

import static gov.nist.csd.pm.policy.model.access.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
import static gov.nist.csd.pm.policy.model.graph.nodes.Properties.NO_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueTest {

    @Test
    void testStringToValue() throws PMException {
        Value value = Value.fromObject("test");
        assertTrue(value.getType().isString());
        assertEquals("test", value.getStringValue());
    }

    @Test
    void testArrayToValue() throws PMException {
        Value value = Value.fromObject(List.of("hello", "world"));
        assertTrue(value.getType().isArray());
        assertEquals(new StringValue("hello"), value.getArrayValue().get(0));
        assertEquals(new StringValue("world"), value.getArrayValue().get(1));
    }

    @Test
    void testBooleanToValue() throws PMException {
        Value value = Value.fromObject(true);
        assertTrue(value.getType().isBoolean());
        assertTrue(value.getBooleanValue());
    }

    @Test
    void testListToValue() throws PMException {
        Value value = Value.fromObject(Arrays.asList("hello", "world"));
        assertTrue(value.getType().isArray());
        assertEquals(new StringValue("hello"), value.getArrayValue().get(0));
        assertEquals(new StringValue("world"), value.getArrayValue().get(1));
    }

    @Test
    void testObjectToValue() throws PMException {
        EventContext testEventCtx = new EventContext(new UserContext("testUser"), "target123",
                                                     new CreateObjectAttributeEvent("testOA", NO_PROPERTIES, "pc1")
        );

        Value objectToValue = Value.fromObject(testEventCtx);
        assertTrue(objectToValue.getType().isMap());

        Value key = new StringValue("userCtx");
        Value value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isMap());
        assertEquals(
                Map.of(
                        new StringValue("user"), new StringValue("testUser"), new StringValue("process"),
                        new StringValue("")
                ),
                value.getMapValue()
        );

        key = new StringValue("target");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isString());
        assertEquals(
                "target123",
                value.getStringValue()
        );

        key = new StringValue("eventName");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isString());
        assertEquals(
                CREATE_OBJECT_ATTRIBUTE,
                value.getStringValue()
        );

        key = new StringValue("event");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isMap());
        assertEquals(
                Map.of(new StringValue("name"), new StringValue("testOA"),
                       new StringValue("type"), new StringValue("OA"),
                       new StringValue("properties"), new MapValue(new HashMap<>(), Type.string(), Type.string()),
                       new StringValue("initialParent"), new StringValue("pc1"),
                       new StringValue("additionalParents"), new ArrayValue(new ArrayList<>(), Type.string()),
                       new StringValue("eventName"), new StringValue("create_object_attribute")
                ),
                value.getMapValue()
        );
    }
}
