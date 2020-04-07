package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.Condition;
import gov.nist.csd.pm.pip.obligations.model.NegatedCondition;

public class Action {
    private Condition condition;
    private NegatedCondition negatedCondition;

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
}
