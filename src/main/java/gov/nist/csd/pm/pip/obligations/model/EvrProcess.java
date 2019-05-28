package gov.nist.csd.pm.pip.obligations.model;

import gov.nist.csd.pm.pip.obligations.model.functions.Function;

public class EvrProcess {
    private long   value;
    private Function function;

    public EvrProcess(long process) {
        this.value = process;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Function getFunction() {
        return function;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public boolean equals(Object o) {
        if(!(o instanceof EvrProcess)) {
            return false;
        }

        EvrProcess p = (EvrProcess)o;

        if(this.value == 0) {
            return false;
        }

        return this.value == p.value;
    }
}
