package gov.nist.csd.pm.pdp;

import gov.nist.csd.pm.common.exception.PMException;
import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.query.model.explain.Explain;

import java.util.Collection;

public class UnauthorizedException extends PMException {

    private final Explain explain;

    public UnauthorizedException(Explain explain, UserContext user, String target, Collection<String> missingAccessRights) {
        super(userString(user) + " does not have access right " + missingAccessRights + " on " + target);
        this.explain = explain;
    }

    public UnauthorizedException(Explain explain, UserContext user, TargetContext target, Collection<String> missingAccessRights) {
        super(userString(user) + " does not have access right " + missingAccessRights + " on " + target);
        this.explain = explain;
    }

    public Explain getExplain() {
        return explain;
    }

    private static String userString(UserContext user) {
        return String.format("{user: %s%s}", user.getUser(), user.getProcess() != null ? ", process: " + user.getProcess() : "");
    }
}
