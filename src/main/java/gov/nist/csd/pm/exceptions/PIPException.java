package gov.nist.csd.pm.exceptions;

public class PIPException extends PMException {
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
