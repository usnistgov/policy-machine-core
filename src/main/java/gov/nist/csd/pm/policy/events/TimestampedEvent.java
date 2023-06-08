package gov.nist.csd.pm.policy.events;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

public record TimestampedEvent(long timestamp, byte[] bytes) implements Serializable {

    public byte[] serialize() {
        return SerializationUtils.serialize(this);
    }

    public static TimestampedEvent fromBytes(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }

}
