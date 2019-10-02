package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

public interface FunctionExecutor {
    String getFunctionName();
    int numParams();
    Object exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException;
}