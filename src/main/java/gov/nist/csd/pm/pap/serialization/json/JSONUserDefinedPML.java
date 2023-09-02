package gov.nist.csd.pm.pap.serialization.json;

import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

public class JSONUserDefinedPML {
    Map<String, String> functions;
    Map<String, String> constants;

    public JSONUserDefinedPML() {
    }

    public JSONUserDefinedPML(Map<String, String> functions, Map<String, String> constants) {
        this.functions = functions;
        this.constants = constants;
    }

    public Map<String, String> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, String> functions) {
        this.functions = functions;
    }

    public Map<String, String> getConstants() {
        return constants;
    }

    public void setConstants(Map<String, String> constants) {
        this.constants = constants;
    }
}
