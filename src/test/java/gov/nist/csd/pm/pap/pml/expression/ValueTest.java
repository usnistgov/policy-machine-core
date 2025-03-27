package gov.nist.csd.pm.pap.pml.expression;

import gov.nist.csd.pm.common.event.EventContext;
import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.executable.op.graph.CreateObjectAttributeOp;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.ArrayValue;
import gov.nist.csd.pm.pap.pml.value.MapValue;
import gov.nist.csd.pm.pap.pml.value.StringValue;
import gov.nist.csd.pm.pap.pml.value.Value;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gov.nist.csd.pm.common.graph.node.Properties.NO_PROPERTIES;
import static gov.nist.csd.pm.pap.AdminAccessRights.CREATE_OBJECT_ATTRIBUTE;
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
        EventContext testEventCtx = new EventContext(
                "testUser",
                "",
                new CreateObjectAttributeOp().getName(),
                Map.of("name", "testOA", "properties", NO_PROPERTIES, "descendants", List.of("pc1"))
        );

        Value objectToValue = Value.fromObject(testEventCtx);
        assertTrue(objectToValue.getType().isMap());

        Value key = new StringValue("user");
        Value value = objectToValue.getMapValue().get(key);
        assertEquals(new StringValue("testUser"), value);

        key = new StringValue("opName");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isString());
        assertEquals(CREATE_OBJECT_ATTRIBUTE, value.getStringValue());

        key = new StringValue("operands");
        value = objectToValue.getMapValue().get(key);
        assertTrue(value.getType().isMap());
        assertEquals(
                Map.of(new StringValue("name"), new StringValue("testOA"),
                        new StringValue("properties"), new MapValue(new HashMap<>(), Type.string(), Type.string()),
                        new StringValue("descendants"), new ArrayValue(List.of(new StringValue("pc1")), Type.string())
                ),
                value.getMapValue()
        );
    }
}
