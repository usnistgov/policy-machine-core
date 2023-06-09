package gov.nist.csd.pm.policy.events;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;

public class TimestampedEvent implements Serializable {

    private long timestamp;
    private byte[] bytes;
    private byte[] hash;

    public TimestampedEvent(long timestamp, byte[] bytes) throws NoSuchAlgorithmException {
        this.timestamp = timestamp;
        this.bytes = bytes;
        this.hash = hashEvent();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] serialize() {
        return SerializationUtils.serialize(this);
    }

    private byte[] hashEvent() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(String.valueOf(timestamp).getBytes());
        digest.update(bytes);

        return digest.digest();
    }

    public static TimestampedEvent fromBytes(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
