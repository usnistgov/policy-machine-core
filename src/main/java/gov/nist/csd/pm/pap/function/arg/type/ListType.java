package gov.nist.csd.pm.pap.function.arg.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListType<E> extends Type<List<E>> {

    private final Type<E> elementType;

    public ListType(Type<E> elementType) {
        this.elementType = elementType;
    }

    public Type<E> getElementType() {
        return elementType;
    }

    @Override
    public List<E> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof List<?> sourceList)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to List");
        }
        List<E> resultList = new ArrayList<>();
        for (Object element : sourceList) {
            resultList.add(elementType.cast(element));
        }
        return resultList;
    }

    @Override
    public Class<List<E>> getExpectedClass() {
        return (Class<List<E>>)(Class<?>) List.class;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListType<?> listType)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(elementType, listType.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), elementType);
    }
}
