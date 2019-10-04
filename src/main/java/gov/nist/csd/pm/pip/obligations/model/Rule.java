package gov.nist.csd.pm.pip.obligations.model;

public class Rule {
    private String label;
    private EventPattern eventPattern;
    private ResponsePattern responsePattern;

    public Rule() {
        this.label = "";
        this.eventPattern = new EventPattern();
        this.responsePattern = new ResponsePattern();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EventPattern getEventPattern() {
        return eventPattern;
    }

    public void setEventPattern(EventPattern eventPattern) {
        this.eventPattern = eventPattern;
    }

    public ResponsePattern getResponsePattern() {
        return responsePattern;
    }

    public void setResponsePattern(ResponsePattern responsePattern) {
        this.responsePattern = responsePattern;
    }
}
