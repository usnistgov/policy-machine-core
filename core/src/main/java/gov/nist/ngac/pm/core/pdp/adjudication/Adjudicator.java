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

package gov.nist.ngac.pm.core.pdp.adjudication;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.common.exception.PMRuntimeException;
import gov.nist.ngac.pm.core.pap.PAP;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.operation.accessright.AdminAccessRight;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import gov.nist.ngac.pm.core.pdp.UnauthorizedException;
import java.util.Collection;

/**
 * Base class for PDP adjudicators, which wrap a {@link PAP} query/modification sub-area with a required
 * access right check before delegating.
 */
public abstract class Adjudicator {

    protected PAP pap;
    protected UserContext userCtx;

    public Adjudicator(PAP pap, UserContext userCtx) {
        this.pap = pap;
        this.userCtx = userCtx;
    }

    /**
     * Checks that the user holds every required access right on the target.
     *
     * @param userCtx the user to check
     * @param targetCtx the target to check privileges against
     * @param required the access rights the user must hold
     * @throws UnauthorizedException if the user is missing any required access right, or holds none at all
     * @throws PMException if computing the user's privileges fails
     */
    protected void check(UserContext userCtx, TargetContext targetCtx, AdminAccessRight ... required) throws PMException {
        AccessRightSet requiredSet = new AccessRightSet(required);
        AccessRightSet computed = pap.query().access().computePrivileges(userCtx, targetCtx);

        if (computed.containsAll(requiredSet) && !computed.isEmpty()) {
            return;
        }

        throw UnauthorizedException.of(pap.query().graph(), userCtx, targetCtx, computed, requiredSet);
    }

    /**
     * Callback invoked per item by {@link #filterUnauthorized}.
     *
     * @param <T> the item type
     */
    @FunctionalInterface
    protected interface PMConsumer<T> {
        void accept(T t) throws PMException;
    }

    /**
     * Removes any item from items for which checkFn throws {@link UnauthorizedException}.
     * Other {@link PMException}s propagate as checked, not wrapped in a RuntimeException.
     *
     * @param <T> the item type
     * @param items the collection to filter in place
     * @param checkFn the per-item check; an {@link UnauthorizedException} removes the item
     * @throws PMException if checkFn throws any exception other than {@link UnauthorizedException}
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
