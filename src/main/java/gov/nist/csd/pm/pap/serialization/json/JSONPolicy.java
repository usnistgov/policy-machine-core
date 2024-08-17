package gov.nist.csd.pm.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.pap.graph.relationship.AccessRightSet;

import java.util.List;

public class JSONPolicy {

    private AccessRightSet resourceOperations;
    private JSONGraph graph;
    private List<String> prohibitions;
    private List<String> obligations;
    private List<String> operations;
    private List<String> routines;

    public JSONPolicy(AccessRightSet resourceOperations, JSONGraph graph, List<String> prohibitions, List<String> obligations, List<String> operations, List<String> routines) {
        this.resourceOperations = resourceOperations;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
        this.routines = routines;
    }

    public AccessRightSet getResourceOperations() {
        return resourceOperations;
    }

    public void setResourceOperations(AccessRightSet resourceOperations) {
        this.resourceOperations = resourceOperations;
    }

    public JSONGraph getGraph() {
        return graph;
    }

    public void setGraph(JSONGraph graph) {
        this.graph = graph;
    }

    public List<String> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<String> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<String> getObligations() {
        return obligations;
    }

    public void setObligations(List<String> obligations) {
        this.obligations = obligations;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }

    public List<String> getRoutines() {
        return routines;
    }

    public void setRoutines(List<String> routines) {
        this.routines = routines;
    }

    @Override
    public String toString() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create()
                .toJson(this);
    }

    public static JSONPolicy fromJSON(String json) {
        return new Gson().fromJson(json, JSONPolicy.class);
    }
}
