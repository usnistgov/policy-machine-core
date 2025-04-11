package gov.nist.csd.pm.pap.function.arg.type;

public final class ObjectType extends ArgType<Object> {

    @Override
    public Object cast(Object obj) {
        return obj;
    }

    /**
     * Helper method to safely cast an Object to a specific target type
     * 
     * @param obj The object to cast
     * @param targetType The target ArgType to cast to
     * @param <T> The Java type of the target
     * @return The object cast to type T
     */
    public <T> T castTo(Object obj, ArgType<T> targetType) {
        if (obj == null) {
            return null;
        }
        
        // Let the target type handle the casting logic
        return targetType.cast(obj);
    }

    @Override
    public Class<Object> getExpectedClass() {
        return Object.class;
    }
}
