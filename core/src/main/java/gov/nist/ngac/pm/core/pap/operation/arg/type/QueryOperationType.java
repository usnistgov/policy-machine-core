package gov.nist.ngac.pm.core.pap.operation.arg.type;

import gov.nist.ngac.pm.core.pap.operation.QueryOperation;

/**
 * The PML type of a first-class reference to a {@link QueryOperation}.
 */
public final class QueryOperationType extends Type<QueryOperation<?>> {

    @Override
    public QueryOperation<?> cast(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        if (!(obj instanceof QueryOperation<?> o)) {
            throw new IllegalArgumentException("Cannot cast " + obj.getClass() + " to QueryOperation");
        }

        return o;
    }
}
