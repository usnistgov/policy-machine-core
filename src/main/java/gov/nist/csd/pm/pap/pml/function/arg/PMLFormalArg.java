package gov.nist.csd.pm.pap.pml.function.arg;

import gov.nist.csd.pm.pap.function.arg.FormalArg;
import gov.nist.csd.pm.pap.pml.type.Type;
import gov.nist.csd.pm.pap.pml.value.Value;
import java.util.Objects;

public class PMLFormalArg extends FormalArg<Value> {

    private final Type pmlType;

    public PMLFormalArg(String name, Type pmlType) {
        super(name, new ValueType());
        this.pmlType = pmlType;
    }

    public Type getPmlType() {
        return pmlType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PMLFormalArg that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(pmlType, that.pmlType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), pmlType);
    }
}
