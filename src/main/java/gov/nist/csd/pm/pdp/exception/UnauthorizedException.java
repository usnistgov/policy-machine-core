package gov.nist.csd.pm.pdp.exception;

import gov.nist.csd.pm.pap.query.model.context.TargetContext;
import gov.nist.csd.pm.pap.query.model.context.UserContext;
import gov.nist.csd.pm.pap.exception.PMException;
import gov.nist.csd.pm.pap.query.model.explain.Explain;

import java.util.Collection;

public class UnauthorizedException extends PMException {

    private Explain explain;

    public UnauthorizedException(Explain explain, UserContext user, String target, Collection<String> missingAccessRights) {
        super("[" + user + "] does not have access right " + missingAccessRights + " on [" + target + "]");
        this.explain = explain;
    }

    public UnauthorizedException(Explain explain, UserContext user, TargetContext target, Collection<String> missingAccessRights) {
        super("[" + user + "] does not have access right " + missingAccessRights + " on [" + target + "]");
        this.explain = explain;
    }

    public Explain getExplain() {
        return explain;
    }
}
