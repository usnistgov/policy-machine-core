package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.pip.obligations.model.actions.Action;

import java.util.ArrayList;
import java.util.List;

public class ResponsePattern {

    private Condition    condition;
    private NegatedCondition negatedCondition;
    private List<Action> actions;

    public ResponsePattern() {
        this.actions = new ArrayList<>();
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public NegatedCondition getNegatedCondition() {
        return negatedCondition;
    }

    public void setNegatedCondition(NegatedCondition negatedCondition) {
        this.negatedCondition = negatedCondition;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }
}
