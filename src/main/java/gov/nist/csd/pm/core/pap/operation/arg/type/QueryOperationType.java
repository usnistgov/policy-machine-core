package gov.nist.csd.pm.core.pap.operation.arg.type;

import gov.nist.csd.pm.core.pap.operation.QueryOperation;

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
