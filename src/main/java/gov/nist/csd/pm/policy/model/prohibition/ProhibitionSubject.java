package gov.nist.csd.pm.policy.model.prohibition;

import gov.nist.csd.pm.policy.exceptions.InvalidProhibitionSubjectException;

public class ProhibitionSubject {

    public static ProhibitionSubject userAttribute(String ua) {
        return new ProhibitionSubject(ua, Type.USER_ATTRIBUTE);
    }

    public static ProhibitionSubject user(String user) {
        return new ProhibitionSubject(user, Type.USER);
    }

    public static ProhibitionSubject process(String process) {
        return new ProhibitionSubject(process, Type.PROCESS);
    }

    private final String name;
    private final Type type;

    public ProhibitionSubject(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public ProhibitionSubject(String name, String type) throws InvalidProhibitionSubjectException {
        this.name = name;
        this.type = typeFromString(type);
    }

    public String name() {
        return name;
    }

    public Type type() {
        return type;
    }

    public enum Type {
        USER_ATTRIBUTE,
        USER,
        PROCESS
    }

    private Type typeFromString(String s) throws InvalidProhibitionSubjectException {
        switch (s) {
            case "USER_ATTRIBUTE" -> {
                return Type.USER_ATTRIBUTE;
            }
            case "USER" -> {
                return Type.USER;
            }
            case "PROCESS" -> {
                return Type.PROCESS;
            }
            default -> throw new InvalidProhibitionSubjectException(s);
        }
    }
}
