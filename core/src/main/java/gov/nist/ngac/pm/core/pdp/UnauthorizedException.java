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

package gov.nist.ngac.pm.core.pdp;

import gov.nist.ngac.pm.core.common.exception.PMException;
import gov.nist.ngac.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.ngac.pm.core.pap.query.GraphQuery;
import gov.nist.ngac.pm.core.pap.query.model.context.AnonymousTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.NodeTargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.TargetContext;
import gov.nist.ngac.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Thrown when a user lacks the privileges required to perform an operation.
 */
public class UnauthorizedException extends PMException {

    /**
     * Builds an exception for a user denied a named operation outright (e.g. no target-specific
     * privileges apply).
     */
    public static UnauthorizedException of(GraphQuery graphQuery,
                                           UserContext userContext,
                                           String operation) throws PMException {
        return new UnauthorizedException(userContext + " cannot perform operation " + operation);
    }

    /**
     * Builds an exception with a pre-formatted message.
     */
    public static UnauthorizedException of(String message) throws PMException {
        return new UnauthorizedException(message);
    }

    /**
     * Builds an exception for a user missing one or more required access rights on a target, formatting
     * the target's name(s) and the specific missing rights into the message.
     */
    public static UnauthorizedException of(GraphQuery graphQuery,
                                           UserContext userContext,
                                           TargetContext targetContext,
                                           AccessRightSet has,
                                           Collection<String> required) throws PMException {
        String targetStr = targetString(graphQuery, targetContext);
        AccessRightSet missing = new AccessRightSet(required);
        missing.removeAll(has);

        if (required.isEmpty()) {
            return new UnauthorizedException(userContext + " does not have any access rights on " + targetStr);
        }

        return new UnauthorizedException(userContext + " missing required access rights " + missing + " on " + targetStr);
    }

    private static String targetString(GraphQuery graphQuery, TargetContext targetContext) throws PMException {
        if (targetContext instanceof NodeTargetContext ctx) {
            String name = ctx.getName() != null
                ? ctx.getName()
                : graphQuery.getNodeById(ctx.getId()).getName();
            return "{target: " + name + "}";
        } else {
            AnonymousTargetContext ctx = (AnonymousTargetContext) targetContext;
            List<String> names = new ArrayList<>();
            if (ctx.getAttributeNames() != null) {
                names.addAll(ctx.getAttributeNames());
            } else {
                for (long id : ctx.getAttributeIds()) {
                    names.add(graphQuery.getNodeById(id).getName());
                }
            }
            return String.format("{target: [%s]}", String.join(", ", names));
        }
    }

    private UnauthorizedException(String msg) {
        super(msg);
    }
}
