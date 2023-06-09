package gov.nist.csd.pm.policy.events;

public class PolicyEventRequestWithTimestamp extends PolicyEventRequest {

    private long timestamp;

    public PolicyEventRequestWithTimestamp(byte[] previousHash, byte[] events, long timestamp) {
        super(previousHash, events);

        this.timestamp = timestamp;
    }

    public PolicyEventRequestWithTimestamp(byte[] previousHash, PolicyEventList eventList, long timestamp) {
        super(previousHash, eventList);

        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
