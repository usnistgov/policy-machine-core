package gov.nist.csd.pm.policy.events;

import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class PolicyEventRequest implements Serializable {

    private byte[] previousEventHash;
    private byte[] events;

    public PolicyEventRequest(byte[] previousEventHash, byte[] events) {
        this.previousEventHash = previousEventHash;
        this.events = events;
    }

    public PolicyEventRequest(byte[] previousEventHash, PolicyEventList eventList) {
        this.previousEventHash = previousEventHash;
        this.events = SerializationUtils.serialize(eventList);
    }

    public byte[] getPreviousEventHash() {
        return previousEventHash;
    }

    public void setPreviousEventHash(byte[] previousEventHash) {
        this.previousEventHash = previousEventHash;
    }

    public byte[] getEvents() {
        return events;
    }

    public void setEvents(byte[] events) {
        this.events = events;
    }
}
