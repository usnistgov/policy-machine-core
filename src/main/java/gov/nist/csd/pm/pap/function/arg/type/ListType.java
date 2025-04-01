package gov.nist.csd.pm.pap.function.arg.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ListType<E> extends ArgType<List<E>> {
    private final ArgType<E> elementType;

    public ListType(ArgType<E> elementType) {
        this.elementType = elementType;
    }

    public ArgType<E> getElementType() {
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
