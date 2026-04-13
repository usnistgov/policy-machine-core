package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import gov.nist.csd.pm.core.pap.query.GraphQuery;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeIdsTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.AttributeNamesTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.IdTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.NameTargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UnauthorizedException extends PMException {

    public static UnauthorizedException of(GraphQuery graphQuery,
                                           UserContext userContext,
                                           String operation) throws PMException {
        return new UnauthorizedException(userContext + " cannot perform operation " + operation);
    }

    public static UnauthorizedException of(String message) throws PMException {
        return new UnauthorizedException(message);
    }

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
        return switch (targetContext) {
            case IdTargetContext ctx -> "{target: " + graphQuery.getNodeById(ctx.targetId()).getName() + "}";
            case NameTargetContext ctx -> "{target: " + ctx.targetName() + "}";
            case AttributeIdsTargetContext ctx -> {
                List<String> names = new ArrayList<>();
                for (long id : ctx.attributeIds()) {
                    names.add(graphQuery.getNodeById(id).getName());
                }
                yield String.format("{target: [%s]}", String.join(", ", names));
            }
            case AttributeNamesTargetContext ctx ->
                String.format("{target: [%s]}", String.join(", ", ctx.attributeNames()));
        };
    }

    private UnauthorizedException(String msg) {
        super(msg);
    }
}
