package gov.nist.ngac.pm.core.pdp.adjudication;

import gov.nist.ngac.pm.core.common.graph.node.Node;
import java.util.List;
import java.util.Map;

/**
 * The nodes produced by a resource operation, keyed by the formal parameter name they satisfy.
 */
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

    /**
     * Records the nodes satisfying the given formal parameter name.
     *
     * @return this instance, for chaining
     */
    public ResourceOperationResult addResult(String argName, List<Node> nodes) {
        this.result.put(argName, nodes);
        return this;
    }
}
