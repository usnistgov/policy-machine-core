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
