package gov.nist.csd.pm.pdp.services;

public class UserContext {
    private String user;
    private String process;

    public UserContext(String user, String process) {
        this.user = user;
        this.process = process;
    }

    public UserContext(String user) {
        this.user = user;
        this.process = "";
    }

    public String getUser() {
        return user;
    }

    public String getProcess() {
        return process;
    }
}
