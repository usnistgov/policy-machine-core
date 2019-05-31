package gov.nist.csd.pm.pip.obligations.model.functions;

public class Arg {
    private String value;
    private Function function;

    public Arg(String value) {
        this.value = value;
    }

    public Arg(Function function) {
        this.function = function;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }
}
