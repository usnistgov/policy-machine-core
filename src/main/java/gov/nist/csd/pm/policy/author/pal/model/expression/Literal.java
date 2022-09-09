package gov.nist.csd.pm.policy.author.pal.model.expression;

import gov.nist.csd.pm.policy.author.pal.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.author.pal.statement.Expression;
import gov.nist.csd.pm.policy.author.pal.statement.PALStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;
import gov.nist.csd.pm.policy.author.PolicyAuthor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Literal extends PALStatement {

    private boolean isStringLiteral;
    private String stringLiteral;

    private boolean isBooleanLiteral;
    private boolean booleanLiteral;

    private boolean isArrayLiteral;
    private ArrayLiteral arrayLiteral;

    private boolean isMapLiteral;
    private MapLiteral mapLiteral;

    private final Type type;

    public Literal(String stringLiteral) {
        this.isStringLiteral = true;
        this.stringLiteral = stringLiteral;
        this.type = Type.string();
    }

    public Literal(boolean booleanLiteral) {
        this.isBooleanLiteral = true;
        this.booleanLiteral = booleanLiteral;
        this.type = Type.bool();
    }

    public Literal(ArrayLiteral arrayLiteral) {
        this.isArrayLiteral = true;
        this.arrayLiteral = arrayLiteral;
        this.type = arrayLiteral.getType();
    }

    public Literal(MapLiteral mapLiteral) {
        this.isMapLiteral = true;
        this.mapLiteral = mapLiteral;
        this.type = mapLiteral.getType();
    }

    public boolean isStringLiteral() {
        return isStringLiteral;
    }

    public boolean isBooleanLiteral() {
        return isBooleanLiteral;
    }

    public boolean isArrayLiteral() {
        return isArrayLiteral;
    }

    public boolean isMapLiteral() {
        return isMapLiteral;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public boolean getBooleanLiteral() {
        return booleanLiteral;
    }

    public ArrayLiteral getArrayLiteral() {
        return arrayLiteral;
    }

    public MapLiteral getMapLiteral() {
        return mapLiteral;
    }

    public Type getType() {
        return type;
    }

    @Override
    public Value execute(ExecutionContext ctx, PolicyAuthor policyAuthor) throws PMException {
        if (isStringLiteral()) {
            return new Value(getStringLiteral());
        } else if (isArrayLiteral()) {
            ArrayLiteral arrayLiteral = getArrayLiteral();
            Expression[] exprArr = arrayLiteral.getArray();
            Value[] values = new Value[exprArr.length];
            for (int i = 0; i < exprArr.length; i++) {
                Expression expr = exprArr[i];
                values[i] = expr.execute(ctx, policyAuthor);
            }
            return new Value(values);
        } else if (isBooleanLiteral()) {
            return new Value(getBooleanLiteral());
        } else {
            MapLiteral mapLiteral = getMapLiteral();
            Map<Expression, Expression> map = mapLiteral.getMap();
            Map<Value, Value> values = new HashMap<>();
            for (Expression key : map.keySet()) {
                Expression expr = map.get(key);
                values.put(key.execute(ctx, policyAuthor), expr.execute(ctx, policyAuthor));
            }
            return new Value(values);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return isStringLiteral == literal.isStringLiteral
                && isBooleanLiteral == literal.isBooleanLiteral
                && booleanLiteral == literal.booleanLiteral
                && isArrayLiteral == literal.isArrayLiteral
                && isMapLiteral == literal.isMapLiteral
                && Objects.equals(stringLiteral, literal.stringLiteral)
                && Objects.equals(arrayLiteral, literal.arrayLiteral)
                && Objects.equals(mapLiteral, literal.mapLiteral)
                && Objects.equals(type, literal.type);
    }

    @Override
    public int hashCode() {
        if (isStringLiteral) {
            return stringLiteral.hashCode();
        } else if (isBooleanLiteral) {
            return Objects.hash(booleanLiteral);
        } else if (isArrayLiteral) {
            return arrayLiteral.hashCode();
        } else {
            return mapLiteral.hashCode();
        }
    }

    @Override
    public String toString() {
        if (isStringLiteral) {
            return String.format("'%s'", getStringLiteral());
        } else if (isBooleanLiteral) {
            return String.valueOf(getBooleanLiteral());
        } else if (isArrayLiteral) {
            return getArrayLiteral().toString();
        } else {
            return getMapLiteral().toString();
        }
    }
}
