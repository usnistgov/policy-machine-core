package gov.nist.csd.pm.pip.obligations.model;

import java.util.List;

public class Subject {
    private String       user;
    private List<String> anyUser;
    private EvrProcess    process;

    public Subject() {}

    public Subject(String user) {
        this.user = user;
    }

    public Subject(List<String> users) {
        this.anyUser = users;
    }

    public Subject(EvrProcess process) {
        this.process = process;
    }

    public String getUser() {
        return user;
    }

    public List<String> getAnyUser() {
        return anyUser;
    }

    public EvrProcess getProcess() {
        return process;
    }
}
