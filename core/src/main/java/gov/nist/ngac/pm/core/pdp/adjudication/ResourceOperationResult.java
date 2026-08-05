package gov.nist.ngac.pm.core.pdp.adjudication;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import java.util.List;
import java.util.Map;

public class ResourceOperationResult {

    private Map<String, List<Node>> result;

    public ResourceOperationResult(Map<String, List<Node>> result) {
        this.result = result;
    }

    public ResourceOperationResult() {
    }

    public Map<String, List<Node>> getResult() {
        return result;
    }

    public void setResult(Map<String, List<Node>> result) {
        this.result = result;
    }

    public ResourceOperationResult addResult(String argName, List<Node> nodes) {
        this.result.put(argName, nodes);
        return this;
    }
}
