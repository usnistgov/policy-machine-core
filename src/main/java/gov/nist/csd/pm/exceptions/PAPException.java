package gov.nist.csd.pm.exceptions;

public class PAPException extends PMException {
    private String source;

    public PAPException(String msg) {
        super(msg);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
