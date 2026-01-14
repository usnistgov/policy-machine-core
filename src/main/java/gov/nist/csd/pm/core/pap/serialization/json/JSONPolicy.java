package gov.nist.csd.pm.core.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.graph.relationship.AccessRightSet;

import gov.nist.csd.pm.core.pap.function.op.ResourceOperation;
import java.util.List;

public class JSONPolicy {

    private AccessRightSet resourceAccessRights;
    private JSONGraph graph;
    private List<JSONProhibition> prohibitions;
    private List<JSONObligation> obligations;
    private List<JSONResourceOperation> resourceOperations;
    private List<String> adminOperations;
    private List<String> routines;

    public JSONPolicy(AccessRightSet resourceAccessRights,
                      JSONGraph graph,
                      List<JSONProhibition> prohibitions,
                      List<JSONObligation> obligations,
                      List<JSONResourceOperation> resourceOperations,
                      List<String> adminOperations,
                      List<String> routines) {
        this.resourceAccessRights = resourceAccessRights;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.resourceOperations = resourceOperations;
        this.adminOperations = adminOperations;
        this.routines = routines;
    }

    public AccessRightSet getResourceAccessRights() {
        return resourceAccessRights;
    }

    public void setResourceAccessRights(AccessRightSet resourceAccessRights) {
        this.resourceAccessRights = resourceAccessRights;
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

    public List<JSONResourceOperation> getResourceOperations() {
        return resourceOperations;
    }

    public void setResourceOperations(List<JSONResourceOperation> resourceOperations) {
        this.resourceOperations = resourceOperations;
    }

    public void setObligations(List<JSONObligation> obligations) {
        this.obligations = obligations;
    }

    public List<String> getAdminOperations() {
        return adminOperations;
    }

    public void setAdminOperations(List<String> adminOperations) {
        this.adminOperations = adminOperations;
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
