package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.model.Prohibition;

public class CurrentProcessExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "current_process";
    }

    @Override
    public int numParams() {
        return 0;
    }

    /**
     * The current process is only relevant to prohibitions.  So the function returns a Prohibition.Subject with the
     * current processID and type PROCESS.
     */
    @Override
    public String exec(EventContext eventCtx, String user, String process, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        return process;
    }
}
