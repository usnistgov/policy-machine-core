package gov.nist.csd.pm.policy.pml.expression.reference;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.pml.expression.literal.StringLiteral;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.value.StringValue;
import gov.nist.csd.pm.policy.pml.value.Value;

import java.util.Map;

public class ReferenceByDotIndex extends ReferenceByIndex{

    private final String key;

    public ReferenceByDotIndex(VariableReference varRef, String index) {
        super(varRef, new ReferenceByID(index));
        this.key = index;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        Value value = varRef.execute(ctx, policy);
        if (!value.getType().isMap()) {
            return value;
        }

        Map<Value, Value> mapValue = value.getMapValue();

        return mapValue.get(new StringValue(key));
    }

    @Override
    public String toFormattedString(int indentLevel) {
        return varRef + "." + index;
    }
}
