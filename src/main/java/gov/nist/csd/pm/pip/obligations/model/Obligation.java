package gov.nist.csd.pm.pip.obligations.model;

import java.util.ArrayList;
import java.util.List;

public class Obligation {
    private String user;
    private boolean enabled;
    private String     label;
    private List<Rule> rules;
    private String source;

    public Obligation(String user) {
        this.user = user;
        this.rules = new ArrayList<>();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Rule> getRules() {
        return new ArrayList<>(rules);
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
