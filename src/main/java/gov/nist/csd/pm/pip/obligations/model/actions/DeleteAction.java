package gov.nist.csd.pm.pip.obligations.model.actions;

import java.util.List;

public class DeleteAction extends Action {
    private Action       action;
    private List<String> rules;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }
}
