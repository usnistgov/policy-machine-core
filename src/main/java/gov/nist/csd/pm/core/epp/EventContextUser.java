package gov.nist.csd.pm.core.epp;

import java.util.List;
import java.util.Objects;

public class EventContextUser {

    private final String name;
    private final List<String> attrs;
    private final String process;

    public EventContextUser(String name, String process) {
        this.name = name;
        this.attrs = List.of();
        this.process = Objects.requireNonNullElse(process, "");
    }

    public EventContextUser(String name) {
        this.name = name;
        this.attrs = List.of();
        this.process = "";
    }

    public EventContextUser(List<String> attrs, String process) {
        this.name = "";
        this.attrs = attrs;
        this.process = Objects.requireNonNullElse(process, "");
    }

    public EventContextUser(List<String> attrs) {
        this.name = "";
        this.attrs = attrs;
        this.process = "";
    }

    public boolean isUser() {
        return !name.isEmpty();
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
