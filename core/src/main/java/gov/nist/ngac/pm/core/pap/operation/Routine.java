package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import java.util.List;

/**
 * Base class for routine {@link Operation}s that execute a set of admin operations in a single transaction with
 * access checks on each operation call.
 *
 * @param <R> the operation's return type
 */
public abstract non-sealed class Routine<R> extends Operation<R> {

    public Routine(String name,
                   Type<R> returnType,
                   List<FormalParameter<?>> parameters) {
        super(name, returnType, parameters, List.of());
    }
}
