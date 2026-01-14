package gov.nist.csd.pm.core.pdp;

import gov.nist.csd.pm.core.common.exception.PMException;
import gov.nist.csd.pm.core.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.core.pap.query.model.context.UserContext;
import gov.nist.csd.pm.core.pap.query.model.explain.Explain;

import java.util.ArrayList;
import java.util.Collection;

public class UnauthorizedException extends PMException {

    private final Explain explain;

    public UnauthorizedException(Explain explain, UserContext user, String target, Collection<String> missingAccessRights) {
        super(userString(user) + " does not have access right " + new ArrayList<>(missingAccessRights) + " on " + target);
        this.explain = explain;
    }

    public UnauthorizedException(Explain explain, UserContext user, TargetContext target, Collection<String> missingAccessRights) {
        super(userString(user) + " does not have access right " + new ArrayList<>(missingAccessRights) + " on " + target);
        this.explain = explain;
    }

    public UnauthorizedException(UserContext user, TargetContext target, Collection<String> missingAccessRights) {
        super(userString(user) + " does not have access right " + new ArrayList<>(missingAccessRights) + " on " + target);
        this.explain = null;
    }

    public Explain getExplain() {
        return explain;
    }

    private static String userString(UserContext user) {
        if (user.isUserDefined()) {
            return String.format("{user: %s%s}", user.getUser(), user.getProcess() != null ? ", process: " + user.getProcess() : "");
        } else {
            return String.format("{attrs: %s%s}", user.getAttributeIds(), user.getProcess() != null ? ", process: " + user.getProcess() : "");
        }
    }
}
