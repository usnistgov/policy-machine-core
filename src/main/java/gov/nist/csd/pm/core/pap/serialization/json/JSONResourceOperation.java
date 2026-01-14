package gov.nist.csd.pm.core.pap.serialization.json;

import gov.nist.csd.pm.core.pap.function.arg.FormalParameter;
import java.util.List;

public class JSONResourceOperation {

    private String name;
    private List<FormalParameter<?>> parameters;

    public JSONResourceOperation() {
    }

    public JSONResourceOperation(String name, List<FormalParameter<?>> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FormalParameter<?>> getParameters() {
        return parameters;
    }

    public void setParameters(List<FormalParameter<?>> parameters) {
        this.parameters = parameters;
    }
}
