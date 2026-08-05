package gov.nist.csd.pm.core.pdp.adjudication;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.common.exception.PMRuntimeException;
import gov.nist.csd.pm.core.pap.PAP;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pdp.UnauthorizedException;
import java.util.Collection;

public abstract class Adjudicator {

    protected PAP pap;
    protected UserContext userCtx;

    public Adjudicator(PAP pap, UserContext userCtx) {
        this.pap = pap;
        this.userCtx = userCtx;
    }

    protected void check(UserContext userCtx, TargetContext targetCtx, AdminAccessRight ... required) throws PMException {
        AccessRightSet requiredSet = new AccessRightSet(required);
        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetCtx);

        if (computed.containsAll(requiredSet) && !computed.isEmpty()) {
            return;
        }

        throw UnauthorizedException.of(pap.query().graph(), userCtx, targetCtx, computed, requiredSet);
    }

    @FunctionalInterface
    protected interface PMConsumer<T> {
        void accept(T t) throws PMException;
    }

    /**
     * Removes any item from items for which checkFn throws {@link UnauthorizedException}.
     * Other {@link PMException}s propagate as checked, not wrapped in a RuntimeException.
     */
    protected <T> void filterUnauthorized(Collection<T> items, PMConsumer<T> checkFn) throws PMException {
        try {
            items.removeIf(item -> {
                try {
                    checkFn.accept(item);
                    return false;
                } catch (UnauthorizedException e) {
                    return true;
                } catch (PMException e) {
                    throw new PMRuntimeException(e);
                }
            });
        } catch (PMRuntimeException e) {
            throw (PMException) e.getCause();
        }
    }
}
