package gov.nist.csd.pm.policy.pml.model.expression;

import gov.nist.csd.pm.policy.pml.antlr.PMLParser;

import java.io.Serializable;

public class Type implements Serializable {

    private boolean isVoid;
    private boolean isAny;
    private boolean isString;
    private boolean isNumber;
    private boolean isBoolean;
    private boolean isArray;
    private Type arrayType;
    private boolean isMap;
    private Type mapKeyType;
    private Type mapValueType;

    public static Type any() {
        Type type = new Type();
        type.isAny = true;
        return type;
    }

    public static Type string() {
        Type type = new Type();
        type.isString = true;
        return type;
    }

    public static Type number() {
        Type type = new Type();
        type.isNumber = true;
        return type;
    }

    public static Type bool() {
        Type type = new Type();
        type.isBoolean = true;
        return type;
    }

    public static Type array(Type arrayElementsType) {
        Type type = new Type();
        type.isArray = true;
        type.arrayType = arrayElementsType;
        return type;
    }

    public static Type map(Type mapKeyType, Type mapValueType) {
        Type type = new Type();
        type.isMap = true;
        type.mapKeyType = mapKeyType;
        type.mapValueType = mapValueType;
        return type;
    }

    public static Type voidType() {
        Type type = new Type();
        type.isVoid = true;
        return type;
    }

    public static Type toType(PMLParser.VarTypeContext varTypeContext) {
        Type type = null;
        if (varTypeContext instanceof PMLParser.StringTypeContext) {
            type = Type.string();
        } else if (varTypeContext instanceof PMLParser.BooleanTypeContext) {
            type = Type.bool();
        } else if (varTypeContext instanceof PMLParser.ArrayVarTypeContext arrayVarTypeCtx) {
            type = Type.array(toType(arrayVarTypeCtx.arrayType().varType()));
        } else if (varTypeContext instanceof PMLParser.MapVarTypeContext mapVarTypeContext) {
            type = Type.map(
                    toType(mapVarTypeContext.mapType().keyType),
                    toType(mapVarTypeContext.mapType().valueType)
            );
        } else if (varTypeContext instanceof PMLParser.AnyTypeContext) {
            type = Type.any();
        }

        return type;
    }

    public boolean isAny() {
        return isAny;
    }

    public boolean isString() {
        return isString || isAny;
    }

    public boolean isNumber() {
        return isNumber || isAny;
    }

    public boolean isBoolean() {
        return isBoolean || isAny;
    }

    public boolean isArray() {
        return isArray || isAny;
    }

    public boolean isMap() {
        return isMap || isAny;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public Type getArrayType() {
        if (isAny) {
            return Type.any();
        }

        return arrayType;
    }

    public Type getMapKeyType() {
        if (isAny) {
            return Type.any();
        }

        return mapKeyType;
    }

    public Type getMapValueType() {
        if (isAny) {
            return Type.any();
        }

        return mapValueType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Type type) {
            if (this.isAny || type.isAny) {
                return true;
            } else if (isVoid && type.isVoid) {
                return true;
            } else if (isString && type.isString) {
                return true;
            } else if (isNumber && type.isNumber) {
                return true;
            } else if (isBoolean && type.isBoolean) {
                return true;
            } else if (isArray && type.isArray) {
                return arrayType.equals(type.arrayType);
            } else if (isMap && type.isMap) {
                return this.mapKeyType.equals(type.mapKeyType) &&
                        this.mapValueType.equals(type.mapValueType);
            }
        }

        return false;
    }

    @Override
    public String toString() {
        if (isVoid) {
            return "void";
        } else if (isString) {
            return "string";
        } else if (isNumber) {
            return "number";
        } else if (isBoolean) {
            return "boolean";
        } else if (isArray) {
            return "[]" + arrayType.toString();
        } else if (isMap) {
            return "map[" + mapKeyType.toString() + "]" + mapValueType.toString();
        } else {
            return "any";
        }
    }
}
