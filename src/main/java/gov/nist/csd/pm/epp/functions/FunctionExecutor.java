package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

public interface FunctionExecutor {

    /**
     * The name of the function
     * @return the name of the function.
     */
    String getFunctionName();

    /**
     * How many parameters are expected.
     * @return the number of parameters this function expects
     */
    int numParams();

    /**
     * Execute the function.
     * @param eventCtx the event that is being processed
     * @param user the name of the user that triggered the event
     * @param process the process that triggered the event
     * @param pdp the PDP to access the underlying policy data
     * @param function the function information
     * @param functionEvaluator a FunctionEvaluator to evaluate a nested functions
     * @return the object that the function is expected to return
     * @throws PMException if there is any error executing the function
     */
    Object exec(EventContext eventCtx, String user, String process, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException;
}