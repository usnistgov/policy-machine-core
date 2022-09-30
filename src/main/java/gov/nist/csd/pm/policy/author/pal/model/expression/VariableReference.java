package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.model.scope.UnknownVariableInScopeException;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.*;

public class VariableReference extends PALStatement {

    private boolean isID;
    private String id;
    private boolean isMapEntryReference;
    private MapEntryReference mapEntryReference;
    private final Type type;

    public VariableReference(String id, Type type) {
        this.isID = true;
        this.id = id;
        this.type = type;
    }

    public VariableReference(MapEntryReference mapEntryReference, Type type) {
        this.isMapEntryReference = true;
        this.mapEntryReference = mapEntryReference;
        this.type = type;
    }

    public boolean isID() {
        return isID;
    }

    public String getID() {
        return id;
    }

    public boolean isMapEntryReference() {
        return isMapEntryReference;
    }

    public MapEntryReference getMapEntryReference() {
        return mapEntryReference;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (isID) {
            try {
                return ctx.scope().getValue(id);
            } catch (UnknownVariableInScopeException e) {
                throw new PMException(e.getMessage());
            }
        }

        List<MapEntryReference> refChain = new ArrayList<>();
        MapEntryReference ref = mapEntryReference;
        refChain.add(ref);
        while (!ref.getMap().isID) {
            MapEntryReference next = ref.getMap().getMapEntryReference();
            refChain.add(0, next);

            ref = next;
        }

        Value value = null;
        for (MapEntryReference mapEntryReference : refChain) {
            if (value == null) {
                Value mapValue = mapEntryReference.getMap().execute(ctx, policyAuthor);
                Value keyValue = mapEntryReference.getKey().execute(ctx, policyAuthor);
                value = mapValue.getMapValue().get(keyValue);
                continue;
            }

            if (!value.isMap()) {
                break;
            }

            Map<Value, Value> mapValue = value.getMapValue();
            Value keyValue = mapEntryReference.getKey().execute(ctx, policyAuthor);
            value = mapValue.get(keyValue);
        }

        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableReference that = (VariableReference) o;
        return isID == that.isID
                && isMapEntryReference == that.isMapEntryReference
                && Objects.equals(id, that.id)
                && Objects.equals(mapEntryReference, that.mapEntryReference)
                && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isID, id, isMapEntryReference, mapEntryReference, type);
    }

    @Override
    public String toString() {
        if (isID) {
            return id;
        } else {
            return mapEntryReference.toString();
        }
    }
}
