package gov.nist.csd.pm.exceptions;

public class EPPException extends PMException {
    private String source;

    public EPPException(String msg) {
        super(msg);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
