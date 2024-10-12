package gov.nist.csd.pm.pap.query.model.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserContext implements Serializable {

    private String user;
    private List<String> attributes;
    private String process;

    public UserContext(String user, String process) {
        this.user = user;
        this.process = process;
    }

    public UserContext(String user) {
        this.user = user;
    }

    public UserContext(List<String> attributes, String process) {
        this.attributes = attributes;
        this.process = process;
    }

    public UserContext(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public boolean isUser() {
        return user != null;
    }

    @Override
    public String toString() {
        String s = "%s";
        if (process != null) {
            s += ", process=" + process + "]";
        }

        if (isUser()) {
            return String.format(s, "user=" + user);
        } else {
            return String.format(s, "attributes=" + attributes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserContext that)) return false;
        return Objects.equals(user, that.user) && Objects.equals(attributes, that.attributes) && Objects.equals(process, that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, attributes, process);
    }
}