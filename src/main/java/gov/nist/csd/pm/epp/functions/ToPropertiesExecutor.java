package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;

import java.util.HashMap;
import java.util.Map;

public class ToPropertiesExecutor implements FunctionExecutor {
    @Override
    public String getFunctionName() {
        return "to_props";
    }

    @Override
    public int numParams() {
        return 0;
    }

    /**
     * The args should all be strings with the format: "key=value"
     * This function takes those strings and returns a Map<String,String> to be used
     * as a node's properties
     */
    @Override
    public Object exec(EventContext eventCtx, long userID, long processID, PDP pdp, Function function, FunctionEvaluator functionEvaluator) throws PMException {
        Map<String, String> props = new HashMap<>();
        for(Arg arg : function.getArgs()) {
            String value = arg.getValue();
            String[] tokens = value.split("=");
            if(tokens.length == 2) {
                props.put(tokens[0], tokens[1]);
            }
        }
        return props;
    }
}
