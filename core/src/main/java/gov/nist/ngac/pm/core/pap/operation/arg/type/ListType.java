/*
 * This Software (Policy Machine) is being made available as a public service by the
 * National Institute of Standards and Technology (NIST), an Agency of the United
 * States Department of Commerce. This software was developed in part by employees of
 * NIST and in part by NIST contractors. Copyright in portions of this software that
 * were developed by NIST contractors has been licensed or assigned to NIST. Pursuant
 * to Title 17 United States Code Section 105, works of NIST employees are not
 * subject to copyright protection in the United States. However, NIST may hold
 * international copyright in software created by its employees and domestic
 * copyright (or licensing rights) in portions of software that were assigned or
 * licensed to NIST. To the extent that NIST holds copyright in this software, it is
 * being made available under the Creative Commons Attribution 4.0 International
 * license (CC BY 4.0). The disclaimers of the CC BY 4.0 license apply to all parts
 * of the software developed or licensed by NIST.
 *
 * ACCESS THE FULL CC BY 4.0 LICENSE HERE:
 * https://creativecommons.org/licenses/by/4.0/legalcode
 */

package gov.nist.ngac.pm.core.pap.operation.arg.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Supported type for Lists.
 *
 * @param <E> the Java type of the list's elements
 */
public final class ListType<E> extends Type<List<E>> {

    /**
     * Builds a list type with the given element type.
     *
     * @param type the element type
     * @return the list type
     */
    public static <T> ListType<T> of(Type<T> type) {
        return new ListType<>(type);
    }

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
