package gov.nist.csd.pm.policy.pml.model.expression;

import gov.nist.csd.pm.policy.Policy;
import gov.nist.csd.pm.policy.pml.model.context.ExecutionContext;
import gov.nist.csd.pm.policy.pml.statement.Expression;
import gov.nist.csd.pm.policy.pml.statement.PMLStatement;
import gov.nist.csd.pm.policy.exceptions.PMException;

import java.util.*;

public class Literal extends PMLStatement {

    private boolean isStringLiteral;
    private String stringLiteral;

    private boolean isBooleanLiteral;
    private boolean booleanLiteral;

    private boolean isArrayLiteral;
    private ArrayLiteral arrayLiteral;

    private boolean isMapLiteral;
    private MapLiteral mapLiteral;

    private boolean isNumberLiteral;
    private int numberLiteral;

    private final Type type;

    public Literal(String stringLiteral) {
        this.isStringLiteral = true;
        this.stringLiteral = stringLiteral;
        this.type = Type.string();
    }

    public Literal(int numberLiteral) {
        this.isNumberLiteral = true;
        this.numberLiteral = numberLiteral;
        this.type = Type.number();
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

    public boolean isNumberLiteral() {
        return isNumberLiteral;
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

    public int getNumberLiteral() {
        return numberLiteral;
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
    public Value execute(ExecutionContext ctx, Policy policy) throws PMException {
        if (isStringLiteral()) {
            return new Value(getStringLiteral());
        } else if (isArrayLiteral()) {
            ArrayLiteral arrayLiteral = getArrayLiteral();
            Expression[] exprArr = arrayLiteral.getArray();
            List<Value> values = new ArrayList<>(exprArr.length);
            for (Expression expr : exprArr) {
                values.add(expr.execute(ctx, policy));
            }
            return new Value(values);
        } else if (isBooleanLiteral()) {
            return new Value(getBooleanLiteral());
        } else if (isMapLiteral()){
            MapLiteral mapLiteral = getMapLiteral();
            Map<Expression, Expression> map = mapLiteral.getMap();
            Map<Value, Value> values = new HashMap<>();
            for (Expression key : map.keySet()) {
                Expression expr = map.get(key);
                values.put(key.execute(ctx, policy), expr.execute(ctx, policy));
            }
            return new Value(values);
        } else {
            return new Value(getNumberLiteral());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return isStringLiteral == literal.isStringLiteral
                && isNumberLiteral == literal.isNumberLiteral
                && isBooleanLiteral == literal.isBooleanLiteral
                && booleanLiteral == literal.booleanLiteral
                && isArrayLiteral == literal.isArrayLiteral
                && isMapLiteral == literal.isMapLiteral
                && Objects.equals(stringLiteral, literal.stringLiteral)
                && numberLiteral == literal.numberLiteral
                && Objects.equals(arrayLiteral, literal.arrayLiteral)
                && Objects.equals(mapLiteral, literal.mapLiteral)
                && Objects.equals(type, literal.type);
    }

    @Override
    public int hashCode() {
        if (isStringLiteral) {
            return stringLiteral.hashCode();
        } else if (isNumberLiteral) {
            return Objects.hash(numberLiteral);
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
        } else if (isNumberLiteral) {
            return String.valueOf(getNumberLiteral());
        } else if (isArrayLiteral) {
            return getArrayLiteral().toString();
        } else {
            return getMapLiteral().toString();
        }
    }
}
