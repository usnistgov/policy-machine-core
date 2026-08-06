package gov.nist.ngac.pm.core.pap.operation;

import gov.nist.ngac.pm.core.pap.operation.arg.type.Type;
import gov.nist.ngac.pm.core.pap.operation.param.FormalParameter;
import java.util.List;

/**
 * Base class for routines: {@link Operation}s that execute with full
 * {@link gov.nist.ngac.pm.core.pap.PAP} access, typically to bundle a sequence of admin operations under
 * one required-capability check.
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
