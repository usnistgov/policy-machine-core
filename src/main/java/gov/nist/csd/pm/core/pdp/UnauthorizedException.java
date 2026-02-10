package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnauthorizedException extends PMException {

    public static UnauthorizedException of(GraphQuery graphQuery,
                                           UserContext userContext,
                                           String operation) throws PMException {
        String userStr = userString(graphQuery, userContext);
        return new UnauthorizedException(userStr + " cannot perform operation " + operation);
    }

    public static UnauthorizedException of(GraphQuery graphQuery,
                                           UserContext userContext,
                                           TargetContext targetContext,
                                           AccessRightSet has,
                                           Collection<String> required) throws PMException {
        String userStr = userString(graphQuery, userContext);
        String targetStr = targetString(graphQuery, targetContext);
        AccessRightSet missing = new AccessRightSet(required);
        missing.removeAll(has);

        if (required.isEmpty()) {
            return new UnauthorizedException(userStr + " does not have any access rights on " + targetStr);
        }

        return new UnauthorizedException(userStr + " missing required access rights " + missing + " on " + targetStr);
    }

    private static String userString(GraphQuery graphQuery, UserContext user) throws PMException {
        if (user.isUserDefined()) {
            String username = graphQuery.getNodeById(user.getUser()).getName();
            return String.format("{user: %s%s}", username, user.getProcess() != null ? ", process: " + user.getProcess() : "");
        } else {
            List<String> attrsNames = new ArrayList<>();
            for (long attrId : user.getAttributeIds()) {
                String attrName = graphQuery.getNodeById(attrId).getName();
                attrsNames.add(attrName);
            }

            return String.format("{user: [%s]%s}", String.join(", ", attrsNames), user.getProcess() != null ? ", process: " + user.getProcess() : "");
        }
    }

    private static String targetString(GraphQuery graphQuery, TargetContext targetContext) throws PMException {
        if (targetContext.isNode()) {
            String username = graphQuery.getNodeById(targetContext.getTargetId()).getName();
            return "{target: " + username + "}";
        } else {
            List<String> attrsNames = new ArrayList<>();
            for (long attrId : targetContext.getAttributeIds()) {
                String attrName = graphQuery.getNodeById(attrId).getName();
                attrsNames.add(attrName);
            }

            return String.format("{target: [%s]}", String.join(", ", attrsNames));
        }
    }

    private UnauthorizedException(String msg) {
        super(msg);
    }
}
