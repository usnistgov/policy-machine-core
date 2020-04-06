package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.List;

public class NegatedCondition {

    private List<Function> condition;

    public List<Function> getCondition() {
        return condition;
    }

    public void setCondition(List<Function> condition) {
        this.condition = condition;
    }

}
