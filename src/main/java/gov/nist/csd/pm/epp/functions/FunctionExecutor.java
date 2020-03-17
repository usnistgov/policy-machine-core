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
     * @param eventCtx The event that is being processed
     * @param userID The ID of the user that triggered the event
     * @param processID The ID of the process that triggered the event
     * @param pdp The PDP to access the underlying policy data
     * @param function The function information
     * @param functionEvaluator A FunctionEvaluator to evaluate a nested functions
     * @return The object that the function is expected to return
     * @throws PMException If there is any error executing the function
     */
    Object exec(EventContext eventCtx, String userID, String processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException;
}