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

    private UnauthorizedException(String msg) {
        super(msg);
    }
}
