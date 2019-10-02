package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

public class CurrentUserToDenySubjectExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "current_user_to_deny_subject";
    }

    @Override
    public int numParams() {
        return 0;
    }

    @Override
    public Object exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        return new Prohibition.Subject(userID, Prohibition.Subject.Type.USER);
    }
}
