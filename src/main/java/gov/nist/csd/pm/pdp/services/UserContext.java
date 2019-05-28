package gov.nist.csd.pm.pdp.services;

public class UserContext {
    private long userID;
    private long processID;

    public UserContext(long userID, long processID) {
        this.userID = userID;
        this.processID = processID;
    }

    public long getUserID() {
        return userID;
    }

    public long getProcessID() {
        return processID;
    }
}
