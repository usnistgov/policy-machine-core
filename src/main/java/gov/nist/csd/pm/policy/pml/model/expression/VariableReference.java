package gov.nist.csd.pm.policy.pml.model.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.*;

public class VariableReference extends PMLStatement {

    private boolean isID;
    private String id;
    private boolean isEntryReference;
    private EntryReference entryReference;
    private final Type type;

    public VariableReference(String id, Type type) {
        this.isID = true;
        this.id = id;
        this.type = type;
    }

    public VariableReference(EntryReference entryReference, Type type) {
        this.isEntryReference = true;
        this.entryReference = entryReference;
        this.type = type;
    }

    public boolean isID() {
        return isID;
    }

    public String getID() {
        return id;
    }

    public boolean isEntryReference() {
        return isEntryReference;
    }

    public EntryReference getEntryReference() {
        return entryReference;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (isID) {
            try {
                return ctx.scope().getValue(id);
            } catch (UnknownVariableInScopeException e) {
                throw new PMException(e.getMessage());
            }
        }

        List<EntryReference> refChain = new ArrayList<>();
        EntryReference ref = entryReference;
        refChain.add(ref);
        while (!ref.getVarRef().isID) {
            EntryReference next = ref.getVarRef().getEntryReference();
            refChain.add(0, next);

            ref = next;
        }

        Value value = null;
        for (EntryReference entRef : refChain) {
            if (value == null) {
                Value mapValue = entRef.getVarRef().execute(ctx, policy);
                Value keyValue = entRef.getKey().execute(ctx, policy);

                if (mapValue.isMap()) {
                    value = mapValue.getMapValue().get(keyValue);
                } else if (mapValue.isArray()) {
                    value = mapValue.getArrayValue().get(keyValue.getNumberValue());
                }

                continue;
            }

            if (!value.isMap() && !value.isArray()) {
                break;
            }

            if (value.isMap()) {
                Map<Value, Value> mapValue = value.getMapValue();
                Value keyValue = entRef.getKey().execute(ctx, policy);
                value = mapValue.get(keyValue);
            } else {
                List<Value> arrayValue = value.getArrayValue();
                Value keyValue = entRef.getKey().execute(ctx, policy);
                value = arrayValue.get(keyValue.getNumberValue());
            }
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableReference that = (VariableReference) o;
        return isID == that.isID
                && isEntryReference == that.isEntryReference
                && Objects.equals(id, that.id)
                && Objects.equals(entryReference, that.entryReference)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isID, id, isEntryReference, entryReference, type);
    }

    @Override
    public String toString() {
        if (isID) {
            return id;
        } else {
            return entryReference.toString();
        }
    }
}
