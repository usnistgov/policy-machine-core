package gov.nist.csd.pm.exceptions;

public class PIPException extends PMException {
    private String source;

    public PIPException(String msg) {
        super(msg);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
