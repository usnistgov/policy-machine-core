package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

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
     * @param graph the graph
     * @param prohibitions the prohibitions
     * @param obligations the obligations
     * @param eventCtx the event that is being processed
     * @param function the function information
     * @param functionEvaluator a FunctionEvaluator to evaluate a nested functions
     * @return the object that the function is expected to return
     * @throws PMException if there is any error executing the function
     */
    Object exec(Graph graph, Prohibitions prohibitions, Obligations obligations,
                EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException;
}