package gov.nist.csd.pm.pap.pml.executable.arg;

import gov.nist.csd.pm.pap.executable.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;

public class PMLFormalArg extends FormalArg<Value> {

    private final Type pmlType;

    public PMLFormalArg(String name, Type pmlType) {
        super(name, Value.class);
        this.pmlType = pmlType;
    }

    public Type getPmlType() {
        return pmlType;
    }
}
