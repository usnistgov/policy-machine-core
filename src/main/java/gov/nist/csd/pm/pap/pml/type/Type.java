package gov.nist.csd.pm.pap.pml.type;

import gov.nist.csd.pm.pap.pml.antlr.PMLParser;

import java.io.Serializable;
import java.util.Objects;

public class Type implements Serializable {

    private boolean isVoid;
    private boolean isAny;
    private boolean isString;
    private boolean isBoolean;
    private boolean isArray;
    private Type arrayElementType;
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

    public static Type bool() {
        Type type = new Type();
        type.isBoolean = true;
        return type;
    }

    public static Type array(Type arrayElementsType) {
        Type type = new Type();
        type.isArray = true;
        type.arrayElementType = arrayElementsType;
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

    public static Type toType(PMLParser.VariableTypeContext varTypeContext) {
        return switch (varTypeContext) {
            case PMLParser.StringTypeContext stringTypeContext -> Type.string();
            case PMLParser.BooleanTypeContext booleanTypeContext -> Type.bool();
            case PMLParser.ArrayVarTypeContext arrayVarTypeCtx ->
                Type.array(toType(arrayVarTypeCtx.arrayType().variableType()));
            case PMLParser.MapVarTypeContext mapVarTypeContext -> Type.map(
                toType(mapVarTypeContext.mapType().keyType),
                toType(mapVarTypeContext.mapType().valueType)
            );
            case null, default -> Type.any();
        };
    }

    public boolean isAny() {
        return isAny;
    }

    public boolean isString() {
        return isString || isAny;
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

    public Type getArrayElementType() {
        if (isAny) {
            return Type.any();
        }

        return arrayElementType;
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
            } else if (isBoolean && type.isBoolean) {
                return true;
            } else if (isArray && type.isArray) {
                return arrayElementType.equals(type.arrayElementType);
            } else if (isMap && type.isMap) {
                return this.mapKeyType.equals(type.mapKeyType) &&
                        this.mapValueType.equals(type.mapValueType);
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAny, isString, isBoolean, isArray, arrayElementType, isMap, mapKeyType, mapValueType);
    }

    @Override
    public String toString() {
        if (isVoid) {
            return "void";
        } else if (isString) {
            return "string";
        } else if (isBoolean) {
            return "bool";
        } else if (isArray) {
            return "[]" + arrayElementType.toString();
        } else if (isMap) {
            return "map[" + mapKeyType.toString() + "]" + mapValueType.toString();
        } else {
            return "any";
        }
    }
}
