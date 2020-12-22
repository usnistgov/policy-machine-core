package gov.nist.csd.pm.epp.functions;

import gov.nist.csd.pm.epp.FunctionEvaluator;
import gov.nist.csd.pm.epp.events.EventContext;
import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.PDP;
import gov.nist.csd.pm.pdp.services.UserContext;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.obligations.Obligations;
import gov.nist.csd.pm.pip.obligations.model.functions.Arg;
import gov.nist.csd.pm.pip.obligations.model.functions.Function;
import gov.nist.csd.pm.pip.prohibitions.Prohibitions;

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

    @Override
    public Map<String, String> exec(Graph graph, Prohibitions prohibitions, Obligations obligations, EventContext eventCtx, Function function, FunctionEvaluator functionEvaluator) throws PMException {
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
