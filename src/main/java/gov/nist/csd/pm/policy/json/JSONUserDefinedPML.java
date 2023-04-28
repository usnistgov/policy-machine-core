package gov.nist.csd.pm.policy.json;

import gov.nist.csd.pm.policy.pml.model.expression.Value;
import gov.nist.csd.pm.policy.pml.statement.FunctionDefinitionStatement;

import java.util.Map;

public class JSONUserDefinedPML {
    Map<String, byte[]> functions;
    Map<String, byte[]> constants;

    public JSONUserDefinedPML() {
    }

    public JSONUserDefinedPML(Map<String, byte[]> functions, Map<String, byte[]> constants) {
        this.functions = functions;
        this.constants = constants;
    }

    public Map<String, byte[]> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, byte[]> functions) {
        this.functions = functions;
    }

    public Map<String, byte[]> getConstants() {
        return constants;
    }

    public void setConstants(Map<String, byte[]> constants) {
        this.constants = constants;
    }
}
