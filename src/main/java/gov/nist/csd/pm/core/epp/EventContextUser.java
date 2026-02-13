package gov.nist.csd.pm.core.epp;

import java.util.List;
import java.util.Objects;

public class EventContextUser {

    private final String name;
    private final List<String> attrs;
    private final String process;

    public EventContextUser(String name, String process) {
        this.name = name;
        this.attrs = null;
        this.process = process;
    }

    public EventContextUser(String name) {
        this.name = name;
        this.attrs = null;
        this.process = null;
    }

    public EventContextUser(List<String> attrs, String process) {
        this.name = null;
        this.attrs = attrs;
        this.process = process;
    }

    public EventContextUser(List<String> attrs) {
        this.name = null;
        this.attrs = attrs;
        this.process = null;
    }

    public boolean isUser() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public String getProcess() {
        return process;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventContextUser that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(attrs, that.attrs)
            && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attrs, process);
    }

    @Override
    public String toString() {
        return "EventContextUser{" +
            "name='" + name + '\'' +
            ", attrs=" + attrs +
            ", process='" + process + '\'' +
            '}';
    }
}
