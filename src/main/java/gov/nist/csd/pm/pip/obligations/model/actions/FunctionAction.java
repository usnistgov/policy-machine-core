package gov.nist.csd.pm.pip.obligations.model.actions;

import gov.nist.csd.pm.pip.obligations.model.functions.Function;

public class FunctionAction extends Action {

    private Function function;

    public FunctionAction(Function function) {
        this.function = function;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
