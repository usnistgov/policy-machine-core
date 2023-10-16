package gov.nist.csd.pm.policy.model.prohibition;

import gov.nist.csd.pm.policy.exceptions.InvalidProhibitionSubjectException;

import java.io.Serializable;
import java.util.Objects;

public class ProhibitionSubject implements Serializable {

    public static ProhibitionSubject userAttribute(String ua) {
        return new ProhibitionSubject(ua, Type.USER_ATTRIBUTE);
    }

    public static ProhibitionSubject user(String user) {
        return new ProhibitionSubject(user, Type.USER);
    }

    public static ProhibitionSubject process(String process) {
        return new ProhibitionSubject(process, Type.PROCESS);
    }

    private String name;
    private Type type;

    public ProhibitionSubject() {}

    public ProhibitionSubject(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public ProhibitionSubject(String name, String type) throws InvalidProhibitionSubjectException {
        this.name = name;
        this.type = typeFromString(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        USER_ATTRIBUTE,
        USER,
        PROCESS
    }

    private Type typeFromString(String s) throws InvalidProhibitionSubjectException {
        switch (s) {
            case "USER_ATTRIBUTE":
                return Type.USER_ATTRIBUTE;
            case "USER":
                return Type.USER;
            case "PROCESS":
                return Type.PROCESS;
            default: throw new InvalidProhibitionSubjectException(s);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProhibitionSubject that = (ProhibitionSubject) o;
        return Objects.equals(name, that.name) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}
