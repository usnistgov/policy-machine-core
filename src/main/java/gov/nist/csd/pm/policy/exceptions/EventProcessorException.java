package gov.nist.csd.pm.policy.exceptions;

public class EventProcessorException extends PMException {
    private String source;

    public EventProcessorException(String msg) {
        super(msg);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
