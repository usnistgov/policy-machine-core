package gov.nist.csd.pm.core.pap.serialization.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gov.nist.csd.pm.core.common.prohibition.Prohibition;
import gov.nist.csd.pm.core.pap.operation.accessright.AccessRightSet;
import java.util.List;

public class JSONPolicy {

    private AccessRightSet resourceAccessRights;
    private JSONGraph graph;
    private List<Prohibition> prohibitions;
    private List<JSONObligation> obligations;
    private JSONOperations operations;

    public JSONPolicy(AccessRightSet resourceAccessRights,
                      JSONGraph graph,
                      List<Prohibition> prohibitions,
                      List<JSONObligation> obligations,
                      JSONOperations operations) {
        this.resourceAccessRights = resourceAccessRights;
        this.graph = graph;
        this.prohibitions = prohibitions;
        this.obligations = obligations;
        this.operations = operations;
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

    public List<Prohibition> getProhibitions() {
        return prohibitions;
    }

    public void setProhibitions(List<Prohibition> prohibitions) {
        this.prohibitions = prohibitions;
    }

    public List<JSONObligation> getObligations() {
        return obligations;
    }

    public void setObligations(List<JSONObligation> obligations) {
        this.obligations = obligations;
    }

    public JSONOperations getOperations() {
        return operations;
    }

    public void setOperations(JSONOperations operations) {
        this.operations = operations;
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
