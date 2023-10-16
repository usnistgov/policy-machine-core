package gov.nist.csd.pm.policy.model.obligation.event;

import java.util.Arrays;

public class Performs {

    private final String[] events;

    public Performs(String... events) {
        this.events = events;
    }

    public String[] events() {
        return events;
    }

    public static Performs events(String ... events) {
        return new Performs(events);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Performs performs = (Performs) o;
        return Arrays.equals(events, performs.events);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(events);
    }
}
