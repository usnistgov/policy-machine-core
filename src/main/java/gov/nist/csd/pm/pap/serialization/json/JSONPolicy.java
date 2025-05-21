package gov.nist.csd.pm.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.common.graph.relationship.AccessRightSet;

import java.util.List;

public class JSONPolicy {

    private AccessRightSet resourceOperations;
    private JSONGraph graph;
    private List<JSONProhibition> prohibitions;
    private List<JSONObligation> obligations;
    private List<String> operations;
    private List<String> routines;

    public JSONPolicy(AccessRightSet resourceOperations,
                      JSONGraph graph,
                      List<JSONProhibition> prohibitions,
                      List<JSONObligation> obligations,
                      List<String> operations,
                      List<String> routines) {
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

    public List<JSONProhibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<JSONProhibition> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<JSONObligation> getObligations() {
        return obligations;
    }

    public void setObligations(List<JSONObligation> obligations) {
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
