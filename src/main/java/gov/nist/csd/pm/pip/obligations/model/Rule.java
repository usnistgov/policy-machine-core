package gov.nist.csd.pm.pip.obligations.model;

public class Rule {
    private String label;
    private Event event;
    private Response response;

    public Rule() {
        this.label = "";
        this.event = new Event();
        this.response = new Response();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
